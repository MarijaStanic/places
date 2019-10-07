package com.marija.diplomski.places.core.domain.data;

import com.marija.diplomski.places.core.domain.model.Keyword;

import java.util.List;

public interface KeywordDao {

    List<Keyword> getAllKeywords();
    boolean addKeyword(Keyword keyword);
    boolean deleteKeyword(Keyword keyword);
}
