package cafe.feestneus;

import java.util.Vector;

public class KjK_QuestionSet
{
        private Vector<KjK_Question> questions = new Vector<KjK_Question>();

        public void addQuestion(KjK_Question question)
        {
        	this.questions.add(question);
        }

        public Vector<KjK_Question> getQuestions()
        {
            return this.questions;
        }   
}