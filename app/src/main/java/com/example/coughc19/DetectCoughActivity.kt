package com.example.coughc19

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.callback.ILoadCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import com.example.coughc19.audioHelper.MFCC
import com.example.coughc19.audioHelper.WavFile
import com.example.coughc19.audioHelper.WavFileException
import com.example.coughc19.databinding.ActivityDetectCoughBinding
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.text.DecimalFormat

class DetectCoughActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetectCoughBinding
    private lateinit var button: ImageButton
    lateinit var mr: MediaRecorder
    private var recording = false
    private val modelPath = "model.tflite"
    val convertedFilePath = Environment.getExternalStorageDirectory().toString() + "/myrec.wav"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectCoughBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )

        binding.btnBack.setOnClickListener(this)
        binding.btnCough.setOnClickListener(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            binding.btnCough.isEnabled = true
    }



    override fun onClick(v: View?) {
        val path = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
        when (v?.id) {
            R.id.btn_back -> {
                val intent = Intent(this@DetectCoughActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_cough -> {
                if (recording){

                }else{
                    mr = MediaRecorder()
                    mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
                    mr.setOutputFile(path)
                    mr.prepare()
                    mr.start()
                    Toast.makeText(this, "Recording cough in 5 seconds..", Toast.LENGTH_SHORT).show()
                    recording = true

                    val timer = object: CountDownTimer(5000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                        }
                        override fun onFinish() {
                            mr.stop()
                            convertToWAV()
                            Toast.makeText(this@DetectCoughActivity, "Stop recording, calculating result", Toast.LENGTH_SHORT).show()
                            val timer2 = object: CountDownTimer(2000, 1000) {
                                override fun onTick(millisUntilFinished: Long) {

                                }
                                override fun onFinish() {
                                    convertToWAV()
                                    val predictedResult = identifyCough(convertedFilePath)
                                    if (predictedResult == "1.0"){
                                        val intentPositive = Intent(this@DetectCoughActivity, IdentifiedCovidActivity::class.java)
                                        startActivity(intentPositive)
                                    }else{
                                        val intentNegative = Intent(this@DetectCoughActivity, DontIdentifiedCovidActivity::class.java)
                                        startActivity(intentNegative)
                                    }
                                }
                            }
                            timer2.start()
                        }
                    }
                    timer.start()
                }

            }
        }
    }

    fun convertToWAV(){
        val nonWAVFile = File(Environment.getExternalStorageDirectory(), "myrec.3gp")
        val callback: IConvertCallback = object : IConvertCallback {
            override fun onSuccess(convertedFile: File?) {
            }

            override fun onFailure(error: java.lang.Exception) {
                error.printStackTrace()
            }
        }
        AndroidAudioConverter.with(this)
            .setFile(nonWAVFile)
            .setFormat(AudioFormat.WAV)
            .setCallback(callback)
            .convert()

        AndroidAudioConverter.load(this, object : ILoadCallback {
            override fun onSuccess() {
            }

            override fun onFailure(error: Exception?) {
                error?.printStackTrace()
            }
        })
    }



    fun identifyCough(audioFilePath: String): String? {

        val mNumFrames: Int
        val mSampleRate: Int
        val mChannels: Int
        var meanMFCCValues : FloatArray = FloatArray(1)

        var predictedResult: String? = ""

        var wavFile: WavFile? = null
        try {
            wavFile = WavFile.openWavFile(File(audioFilePath))
            mNumFrames = wavFile.numFrames.toInt()
            mSampleRate = wavFile.sampleRate.toInt()
            mChannels = wavFile.numChannels
            val buffer =
                Array(mChannels) { DoubleArray(mNumFrames) }

            var frameOffset = 0
            val loopCounter: Int = mNumFrames * mChannels / 4096 + 1
            for (i in 0 until loopCounter) {
                frameOffset = wavFile.readFrames(buffer, mNumFrames, frameOffset)
            }

            val df = DecimalFormat("#.#####")
            df.setRoundingMode(RoundingMode.CEILING)
            val meanBuffer = DoubleArray(mNumFrames)
            for (q in 0 until mNumFrames) {
                var frameVal = 0.0
                for (p in 0 until mChannels) {
                    frameVal = frameVal + buffer[p][q]
                }
                meanBuffer[q] = df.format(frameVal / mChannels).toDouble()
            }


            val mfccConvert = MFCC()
            mfccConvert.setSampleRate(mSampleRate)
            val nMFCC = 5
            mfccConvert.setN_mfcc(nMFCC)
            val mfccInput = mfccConvert.process(meanBuffer)
            val nFFT = mfccInput.size / nMFCC
            val mfccValues =
                Array(nMFCC) { DoubleArray(nFFT) }

            for (i in 0 until nFFT) {
                var indexCounter = i * nMFCC
                val rowIndexValue = i % nFFT
                for (j in 0 until nMFCC) {
                    mfccValues[j][rowIndexValue] = mfccInput[indexCounter].toDouble()
                    indexCounter++
                }
            }

            meanMFCCValues = FloatArray(nMFCC)
            for (p in 0 until nMFCC) {
                var fftValAcrossRow = 0.0
                for (q in 0 until nFFT) {
                    fftValAcrossRow = fftValAcrossRow + mfccValues[p][q]
                }
                val fftMeanValAcrossRow = fftValAcrossRow / nFFT
                meanMFCCValues[p] = fftMeanValAcrossRow.toFloat()
            }


        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: WavFileException) {
            e.printStackTrace()
        }

        predictedResult = loadModelResult(meanMFCCValues)

        return predictedResult

    }



    protected fun loadModelResult(meanMFCCValues: FloatArray) : String? {

        var predictedResult: String? = ""

        val tfliteModel: MappedByteBuffer =
            FileUtil.loadMappedFile(this, modelPath)
        val tflite: Interpreter

        val tfliteOptions =
            Interpreter.Options()
        tfliteOptions.setNumThreads(2)
        tflite = Interpreter(tfliteModel, tfliteOptions)


        val imageTensorIndex = 0
        val imageShape =
            tflite.getInputTensor(imageTensorIndex).shape()
        val imageDataType: DataType = tflite.getInputTensor(imageTensorIndex).dataType()

        val inBuffer: TensorBuffer = TensorBuffer.createDynamic(imageDataType)
        inBuffer.loadArray(meanMFCCValues, imageShape)
        val inpBuffer: ByteBuffer = inBuffer.getBuffer()
        val result = Array(1){FloatArray(1)}
        tflite.run(inpBuffer, result)
        val floatResult : Float = result[0][0]

        predictedResult = floatResult.toString()

        return predictedResult
    }

}

