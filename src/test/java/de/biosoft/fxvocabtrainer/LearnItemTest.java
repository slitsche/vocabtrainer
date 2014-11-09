package de.biosoft.fxvocabtrainer;

import static org.junit.Assert.*;

import org.junit.Test;

public class LearnItemTest {

    
    @Test
    public void failureOn15() {
        LearnItem l = new LearnItem();
        l.setScore(15);
        l.failure();
        assertEquals(15, l.getScore());
    }
    
    @Test
    public void failureOn8(){
        LearnItem l = new LearnItem();
        l.setScore(8);
        int bucket = l.getBucket();
        l.failure();
        assertEquals(bucket + 1, l.getBucket());
        assertEquals(9, l.getScore());
    }
    

}
