package com.marija.diplomski.places.placedetails;

import com.marija.diplomski.places.core.domain.infrastructure.LocationService;
import com.marija.diplomski.places.core.common.Mapper;
import com.marija.diplomski.places.core.domain.model.api.placedetails.Result;

import java.util.LinkedHashMap;
import java.util.List;

public class PlaceDetailsViewModelMapper extends Mapper<Result, PlaceDetailsViewModel> {

    private LocationService locationService;

    public PlaceDetailsViewModelMapper(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public PlaceDetailsViewModel map(Result value) {
        PlaceDetailsViewModel viewModel = new PlaceDetailsViewModel();
        viewModel.setName(value.getName());
        viewModel.setAddress(value.getFormattedAddress());
        viewModel.setWebsite(value.getWebsite());
        viewModel.setPhoneNumber(value.getFormattedPhoneNumber());
        if (value.getOpeningHours() != null) {
            viewModel.setWorkingHours(getWorkingHours(value.getOpeningHours().getWeekdayText()));
        }
        viewModel.setLatitude(locationService.getLatitude());
        viewModel.setLongitude(locationService.getLongitude());
        return viewModel;
    }

    private LinkedHashMap<String, String> getWorkingHours(List<String> weekdayText) {
        LinkedHashMap<String, String> workingHours = new LinkedHashMap<>();
        for (int i = 0; i < weekdayText.size(); i++) {
            String[] dayHours = weekdayText.get(i).split(": ");
            workingHours.put(dayHours[0].trim(), dayHours[1].trim());
        }
        return workingHours;
    }

}