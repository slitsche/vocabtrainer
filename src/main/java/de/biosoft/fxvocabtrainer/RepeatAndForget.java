package de.biosoft.fxvocabtrainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RepeatAndForget {
    private List<Queue<LearnItem>> queues;
    private int lessonUniqueCount;
    private int repeatCount;
    final static int QCOUNT = 4;
    final static int NEW = 3;
    final static int FRESH = 2;
    final static int SOLID = 1;
    final static int GOOD = 0;
    
    public void init(List<LearnItem> items) {
        this.lessonUniqueCount = 0;
        this.repeatCount = 0;
        queues = new ArrayList<Queue<LearnItem>>();
        for (int i = 0; i < QCOUNT; i++) {
            queues.add(new LinkedList<LearnItem>());
        }
        List<Integer> indexes = getIndexes(items.size());
        for (int i = 0; i< items.size(); i++){
            LearnItem l = items.get(indexes.get(i));
            int b = l.getBucket();
            if (b>=0){
                Queue<LearnItem> q = queues.get(b);
                q.offer(l);
            }
        }
    }
    
    /**
     * Returns a randomized List of indexes of size.
     * This will allow iterate a list in random order.
     * @param size
     * @return
     */
    private List<Integer> getIndexes(int size) {
        List<Integer> res = new ArrayList<Integer>(size);
        for (int i = 0; i<size; i++){
            res.add(getShuffleIndex(i), i);
        }
        return res;
    }

    private int getShuffleIndex(int max){
        double r = Math.random();
        int i = (int) (r * max);
        return i;
    }

    public int getCount() {
        int res = 0;
        for (Iterator<Queue<LearnItem>> it = queues.iterator(); it.hasNext();){
            res += it.next().size();
        }
        return res;
    }

    public int getCount(int bucket) {
        return queues.get(bucket).size();
    }

    public LearnItem next() {
        if (getCount(NEW) == 0){
            return null;
        }
        LearnItem l = null;
        while (l == null){
            int b = getCurrentBucket();
            l = queues.get(b).poll();
        }
        if (l != null && !l.isWasRepeated()){
            this.lessonUniqueCount +=1;
        }
        return l;
    }

    /**
     * returns the current bucket based on the distribution
     * - 55% new
     * - 25% fresh
     * - 15% solid
     * - 5% good
     * @return
     */
    public int getCurrentBucket() {
        double r = Math.random();
        if (r > 0.45){ return NEW;}
        else if (r > 0.2) {return FRESH;}
        else if (r > 0.05) {return SOLID;}
        else {return GOOD;}

    }

    public void add(LearnItem l) {
        int b = l.getBucket();
        queues.get(b).offer(l);
        
    }
    public void repeat(LearnItem l) {
        this.repeatCount += 1;
        queues.get(NEW).offer(l);
    }

    public int getLessonUniqueCount() {
        return this.lessonUniqueCount;
    }

    public int getRepeatedCount() {
        return this.repeatCount;
    }

}
