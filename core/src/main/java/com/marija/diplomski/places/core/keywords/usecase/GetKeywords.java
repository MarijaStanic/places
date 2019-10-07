package com.marija.diplomski.places.core.keywords.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.KeywordDao;
import com.marija.diplomski.places.core.domain.model.Keyword;

import java.util.List;

public class GetKeywords extends UseCase<GetKeywords.RequestValues, GetKeywords.ResponseValue> {

    private KeywordDao keywordDao;

    public GetKeywords(KeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Keyword> keywords = keywordDao.getAllKeywords();
        ResponseValue responseValue = new ResponseValue(keywords);
        getUseCaseCallback().onSuccess(responseValue);
    }


    public static final class RequestValues implements UseCase.RequestValues {

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Keyword> mKeywords;

        public ResponseValue(List<Keyword> keywords) {
            mKeywords = keywords;
        }

        public List<Keyword> getKeywords() {
            return mKeywords;
        }
    }
}
