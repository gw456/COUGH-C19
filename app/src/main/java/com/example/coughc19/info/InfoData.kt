package com.example.coughc19.info

object InfoData {

    private val questinFaq = arrayOf(
        "Can COVID-19 affect the respiratory tract?",
        "Can detect COVID-19 using coughing?",
        "What is the appropriate etiquette when coughing or sneezing during COVID-19?",
        "What is community spread?"
    )

    private val answerFaq = arrayOf(
        "COVID-19 can affect the upper respiratory tract (sinuses, nose, and throat) and the lower respiratory tract (windpipe and lungs).",
        "Yes, because a dry cough is a common early symptom of COVID-19. According to some estimates, 60–70%Trusted Source of people who develop COVID-19 symptoms experience a dry cough as an initial symptom.",
        "Cover your mouth and nose with a flexed elbow or tissue when coughing and sneezing. And remember to throw away the used tissue immediately in a bin with a lid and to wash your hands. This way you protect others from any virus released through coughs and sneezes.",
        "Community spread means people have been infected with the virus in an area, including some who are not sure how or where they became infected. Each health department determines community spread differently based on local conditions. For information on community spread in your area, please visit your local health department's website."
    )

    val listData: ArrayList<InfoEntity>
        get() {
            val list = arrayListOf<InfoEntity>()
            for (position in questinFaq.indices) {
                val info = InfoEntity()
                info.question_faq = questinFaq[position]
                info.answer_faq = answerFaq[position]
                list.add(info)
            }
            return list
        }
}