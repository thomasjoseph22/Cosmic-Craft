package com.example.library.service.impl;

import com.example.library.dto.CouponDto;
import com.example.library.model.Coupon;
import com.example.library.repository.CouponRepository;
import com.example.library.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    private CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon save(CouponDto couponDto) {
        Coupon coupon=new Coupon();
        coupon.setCode(couponDto.getCode());
        coupon.setDescription(couponDto.getDescription());
        coupon.setCount(couponDto.getCount());
        coupon.setOffPercentage(couponDto.getOffPercentage());
        coupon.setMaxOff(couponDto.getMaxOff());
        coupon.setExpiryDate(couponDto.getExpiryDate());
        coupon.setEnabled(true);
        couponRepository.save(coupon);


        return coupon;
    }

    @Override
    public List<CouponDto> getAllCoupons() {
        List<Coupon> couponList=couponRepository.findAll();
        List<CouponDto>couponDtoList = transferData(couponList);
        return couponDtoList;
    }

    @Override
    public double applyCoupon(String couponCode, double totalPrice) {
        Coupon coupon= couponRepository.findCouponByCode(couponCode);
        double discountPrice = totalPrice * (coupon.getOffPercentage() / 100.0);
        if(discountPrice > coupon.getMaxOff()){
            discountPrice = coupon.getMaxOff();
        }
        coupon.setCount(coupon.getCount()-1);
        couponRepository.save(coupon);
        double updatedTotalPrice = totalPrice-discountPrice;
        String formattedTotalPrice = String.format("%.2f", updatedTotalPrice);

        return Double.parseDouble(formattedTotalPrice);
    }

    @Override
    public boolean findByCouponCode(String couponCode) {
        Coupon coupon=couponRepository.findCouponByCode(couponCode);
        if(coupon==null){
            return false;
        }else if(!coupon.isEnabled() || coupon.isExpired()){
            return false;
        }
        return true;
    }

    @Override
    public Coupon findByCode(String couponCode) {
        return couponRepository.findCouponByCode(couponCode);
    }

    @Override
    public CouponDto findById(long id) {
        Coupon coupon=couponRepository.findById(id);
        CouponDto couponDto=new CouponDto();
        couponDto.setId(coupon.getId());
        couponDto.setCode(coupon.getCode());
        couponDto.setDescription(coupon.getDescription());
        couponDto.setCount(coupon.getCount());
        couponDto.setOffPercentage(coupon.getOffPercentage());
        couponDto.setMaxOff(coupon.getMaxOff());
        couponDto.setExpiryDate(coupon.getExpiryDate());
        couponDto.setEnabled(coupon.isEnabled());

        return couponDto;
    }

    @Override
    public Coupon update(CouponDto couponDto) {
        long id=couponDto.getId();
        Coupon coupon=couponRepository.findById(id);
        coupon.setCode(couponDto.getCode());
        coupon.setDescription(couponDto.getDescription());
        coupon.setCount(couponDto.getCount());
        coupon.setOffPercentage(couponDto.getOffPercentage());
        coupon.setMaxOff(couponDto.getMaxOff());
        coupon.setExpiryDate(couponDto.getExpiryDate());
        couponRepository.save(coupon);

        return coupon;
    }

    @Override
    public void enable(long id) {
        Coupon coupon=couponRepository.findById(id);
        coupon.setEnabled(true);
        couponRepository.save(coupon);

    }

    @Override
    public void disable(long id) {
        Coupon coupon=couponRepository.findById(id);
        coupon.setEnabled(false);
        couponRepository.save(coupon);

    }

    @Override
    public void deleteCoupon(long id) {

        Coupon coupon=couponRepository.findById(id);
        couponRepository.delete(coupon);

    }

    public List<CouponDto> transferData(List<Coupon> couponList){
        List<CouponDto> couponDtoList=new ArrayList<>();
        for(Coupon coupon : couponList){
            CouponDto couponDto=new CouponDto();
            couponDto.setId(coupon.getId());
            couponDto.setCode(coupon.getCode());
            couponDto.setDescription(coupon.getDescription());
            couponDto.setCount(coupon.getCount());
            couponDto.setOffPercentage(coupon.getOffPercentage());
            couponDto.setMaxOff(coupon.getMaxOff());
            couponDto.setExpiryDate(coupon.getExpiryDate());
            couponDto.setEnabled(coupon.isEnabled());

            couponDtoList.add(couponDto);

        }


        return couponDtoList;
    }


    @Override
    public boolean existsCouponByName(String name) {
        return couponRepository.existsByCode(name);
    }
}
