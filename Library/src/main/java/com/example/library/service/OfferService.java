package com.example.library.service;

import com.example.library.dto.OfferDto;
import com.example.library.model.Offer;

import java.util.List;

public interface OfferService {

    List<OfferDto> getAllOffers();

    Offer Save(OfferDto offerDto);

    OfferDto findById(long id);

    Offer update(OfferDto offerDto);

    void disable(long id);

    void enable(long id);

    void deleteOffer(long id);
}
