package com.home.badpencil.pojo.api.doc;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResDocs {
    public int totalHits;
    public List<HitDoc> scoreDocs;
    private float maxScore;
    public ResDocs() {
    }
    public ResDocs(int totalHits, float maxScore) {
        this.totalHits = totalHits;
        this.scoreDocs = new ArrayList<>();
        this.maxScore = maxScore;
    }

    public void addDoc(HitDoc hitDoc) {
        scoreDocs.add(hitDoc);
    }
}
