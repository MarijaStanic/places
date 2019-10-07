package com.marija.diplomski.places.keywords.viewmodel.mapper;

import android.location.Location;

import com.marija.diplomski.places.core.common.Mapper;
import com.marija.diplomski.places.core.domain.model.api.place.Result;
import com.marija.diplomski.places.core.domain.infrastructure.LocationService;
import com.marija.diplomski.places.keywords.viewmodel.PlaceViewModel;

public class PlaceViewModelMapper extends Mapper<Result, PlaceViewModel> {

    private Location locationA;
    private LocationService locationService;

    public PlaceViewModelMapper(LocationService locationService){
        this.locationService = locationService;
        locationA = new Location("point A");
    }

    @Override
    public PlaceViewModel map(Result value) {
        PlaceViewModel viewModel = new PlaceViewModel();
        viewModel.setLatitude(value.getGeometry().getLocation().getLat());
        viewModel.setLongitude(value.getGeometry().getLocation().getLng());
        viewModel.setVicinity(value.getVicinity());
        viewModel.setName(value.getName());
        viewModel.setPlaceId(value.getPlaceId());
        viewModel.setDistanceToPlace(getDistanceToPlace((value.getGeometry().getLocation())));
        if(value.getOpeningHours() != null) {
            viewModel.setOpen(value.getOpeningHours().getOpenNow());
        }
        return viewModel;
    }

    public String getDistanceToPlace(com.marija.diplomski.places.core.domain.model.api.place.Location location){
        locationA.setLatitude(locationService.getLatitude());
        locationA.setLongitude(locationService.getLongitude());
        Location locationB = new Location("point B");
        locationB.setLatitude(location.getLat());
        locationB.setLongitude(location.getLng());
        Float res = locationA.distanceTo(locationB) / 1000;

        return String.format("%.2f", res);
    }
}
