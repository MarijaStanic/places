package com.marija.diplomski.places.core.keywords.usecase;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.data.KeywordDao;
import com.marija.diplomski.places.core.domain.model.Keyword;

public class AddKeyword extends UseCase<AddKeyword.RequestValues, AddKeyword.ResponseValue> {

    private KeywordDao keywordDao;

    public AddKeyword(KeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        keywordDao.addKeyword(requestValues.getKeyword());
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    public static final class RequestValues implements UseCase.RequestValues {

        private final Keyword keyword;

        public RequestValues(Keyword keyword) {
            this.keyword = keyword;
        }

        public Keyword getKeyword() {
            return keyword;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

    }
}
