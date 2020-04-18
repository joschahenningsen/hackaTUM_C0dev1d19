package com.wimmerth.openvent.data;

import java.util.ArrayList;
import java.util.List;

public class Changes {
    private List<ChangeDetails> data;

    public ArrayList<Change> getChanges(){
        ArrayList<Change> ret = new ArrayList();
        if(data!=null){
            for(ChangeDetails c : data){
                ret.addAll(c.getChanges());
            }
        }
        return ret;
    }
}
