package com.example.library.service.impl;

import com.example.library.dto.OfferDto;
import com.example.library.model.Category;
import com.example.library.model.Offer;
import com.example.library.model.Product;
import com.example.library.repository.OfferRepository;
import com.example.library.repository.ProductRepository;
import com.example.library.service.CategoryService;
import com.example.library.service.OfferService;
import com.example.library.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private OfferRepository offerRepository;

    private ProductService productService;

    private CategoryService categoryService;

    private ProductRepository productRepository;

    public OfferServiceImpl(OfferRepository offerRepository,ProductService productService,
                            CategoryService categoryService, ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productService = productService;
        this.offerRepository = offerRepository;
    }
    @Override
    public List<OfferDto> getAllOffers() {
        List<Offer> offerList=offerRepository.findAll();
        List<OfferDto> offerDtoList=transfer(offerList);


        return offerDtoList;
    }

    @Override
    public Offer Save(OfferDto offerDto) {
        Offer offer = new Offer();
        offer.setName(offerDto.getName());
        offer.setDescription(offerDto.getDescription());
        offer.setOfferType(offerDto.getOfferType());
        offer.setEnabled(true);
        if(offerDto.getOfferType().equals("Product")){
            Product product=productService.findBYId(offerDto.getOfferProductId());
            offer.setOfferProductId(offerDto.getOfferProductId());
            Double oldDiscount= (Double)product.getCostPrice() * ((double)offerDto.getOffPercentage()/100.0);
            String formattedDiscount = String.format("%.2f",oldDiscount);
            Double discount= Double.parseDouble(formattedDiscount);
            String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
            Double salePrice= Double.parseDouble(formattedSalePrice);
            product.setSalePrice(salePrice);
            offer.setApplicableForProductName(product.getName());
            productRepository.save(product);
        }else{
            long applicable_id=offerDto.getOfferCategoryId();
            Category category= categoryService.findById(applicable_id);
            offer.setOfferCategoryId(offerDto.getOfferCategoryId());
            offer.setApplicableForCategoryName(category.getName());
            List<Product> productList = productService.findProductsByCategory(category.getId());
            for(Product product : productList){
                Double oldDiscount= (double)product.getCostPrice() * ((double)offerDto.getOffPercentage()/100.0);
                String formattedDiscount = String.format("%.2f",oldDiscount);
                Double discount= Double.parseDouble(formattedDiscount);
                String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
                Double salePrice= Double.parseDouble(formattedSalePrice);
                product.setSalePrice(salePrice);
                productRepository.save(product);
            }

        }

        offerRepository.save(offer);


        return offer;

    }

    @Override
    public OfferDto findById(long id) {
        Offer offer=offerRepository.findById(id);
        OfferDto offerDto=new OfferDto();
        offerDto.setId(offer.getId());
        offerDto.setName(offer.getName());
        offerDto.setDescription(offer.getDescription());
        offerDto.setOffPercentage(offer.getOffPercentage());
        offerDto.setOfferType(offer.getOfferType());
        if(offer.getOfferType().equals("Product")){
            offerDto.setOfferProductId(offer.getOfferProductId());
            offerDto.setApplicableForProductName(offer.getApplicableForProductName());
        }else{
            offerDto.setOfferCategoryId(offer.getOfferCategoryId());
            offerDto.setApplicableForCategoryName(offer.getApplicableForCategoryName());
        }
        offerDto.setEnabled(offer.isEnabled());


        return offerDto;
    }

    @Override
    public Offer update(OfferDto offerDto) {
        long id=offerDto.getId();
        Offer offer=offerRepository.findById(id);

        offer.setName(offerDto.getName());
        offer.setDescription(offerDto.getDescription());
        offer.setOffPercentage(offerDto.getOffPercentage());
        offer.setOfferType(offerDto.getOfferType());
        if(offerDto.getOfferType().equals("Product")){
            if(offer.getOfferProductId() != null) {
                if (offerDto.getOfferProductId() != offer.getOfferProductId()) {
                    updateProductPrice(offer.getOfferProductId());
                }
            }else{
                updateCategoryPrice(offer.getOfferCategoryId());
                offer.setOfferCategoryId(null);
            }
            Product product=productService.findBYId(offerDto.getOfferProductId());
            offer.setOfferProductId(offerDto.getOfferProductId());
            Double oldDiscount= (Double)product.getCostPrice() * ((double)offerDto.getOffPercentage()/100.0);
            String formattedDiscount = String.format("%.2f",oldDiscount);
            Double discount= Double.parseDouble(formattedDiscount);
            String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
            Double salePrice= Double.parseDouble(formattedSalePrice);
            product.setSalePrice(salePrice);
            offer.setApplicableForProductName(product.getName());
            productRepository.save(product);
        }else{
            if(offer.getOfferCategoryId() != null) {
                if (offerDto.getOfferCategoryId() != offer.getOfferCategoryId()) {
                    updateCategoryPrice(offer.getOfferCategoryId());
                }
            }else{
                updateProductPrice(offer.getOfferProductId());
                offer.setOfferProductId(null);
            }
            long applicable_id=offerDto.getOfferCategoryId();
            Category category= categoryService.findById(applicable_id);
            offer.setOfferCategoryId(offerDto.getOfferCategoryId());
            offer.setApplicableForCategoryName(category.getName());
            List<Product> productList = productService.findProductsByCategory(category.getId());
            for(Product product : productList){
                Double oldDiscount= (Double)product.getCostPrice() * ((double)offerDto.getOffPercentage()/100.0);
                String formattedDiscount = String.format("%.2f",oldDiscount);
                Double discount= Double.parseDouble(formattedDiscount);
                String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
                Double salePrice= Double.parseDouble(formattedSalePrice);
                product.setSalePrice(salePrice);
                productRepository.save(product);
            }


        }

        offerRepository.save(offer);


        return offer;
    }

    public void updateProductPrice(long id){
        Product product=productService.findBYId(id);
        product.setSalePrice(0);
        productRepository.save(product);

    }

    public void updateCategoryPrice(long id){

        Category category= categoryService.findById(id);
        List<Product> productList = productService.findProductsByCategory(category.getId());
        for(Product product : productList){
            product.setSalePrice(0);
            productRepository.save(product);
        }

    }


    @Override
    public void disable(long id) {
        Offer offer=offerRepository.findById(id);
        offer.setEnabled(false);
        if (offer.getOfferType().equals("Product")){
            Product product=productService.findBYId(offer.getOfferProductId());
            product.setSalePrice(0);
            productRepository.save(product);
        }else{
            long applicable_id=offer.getOfferCategoryId();
            Category category= categoryService.findById(applicable_id);
            List<Product> productList = productService.findProductsByCategory(category.getId());
            for(Product product : productList){
                product.setSalePrice(0);
                productRepository.save(product);
            }

        }

    }

    @Override
    public void enable(long id) {
        Offer offer=offerRepository.findById(id);
        offer.setEnabled(true);
        if (offer.getOfferType().equals("Product")){
            Product product=productService.findBYId(offer.getOfferProductId());
            Double oldDiscount= (Double)product.getCostPrice() * ((double)offer.getOffPercentage()/100.0);
            String formattedDiscount = String.format("%.2f",oldDiscount);
            Double discount= Double.parseDouble(formattedDiscount);
            String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
            Double salePrice= Double.parseDouble(formattedSalePrice);
            product.setSalePrice(salePrice);
            productRepository.save(product);
        }else{
            long applicable_id=offer.getOfferCategoryId();
            Category category= categoryService.findById(applicable_id);
            List<Product> productList = productService.findProductsByCategory(category.getId());
            for(Product product : productList){
                Double oldDiscount= (Double)product.getCostPrice() * ((double)offer.getOffPercentage()/100.0);
                String formattedDiscount = String.format("%.2f",oldDiscount);
                Double discount= Double.parseDouble(formattedDiscount);
                String formattedSalePrice = String.format("%.2f", product.getCostPrice() - discount);
                Double salePrice= Double.parseDouble(formattedSalePrice);
                product.setSalePrice(salePrice);
                productRepository.save(product);
            }

        }

    }

    @Override
    public void deleteOffer(long id) {
        Offer offer=offerRepository.findById(id);

        if(offer.getOfferType().equals("Product")){
            Product product=productService.findBYId(offer.getOfferProductId());
            if(product!=null) {
                product.setSalePrice(0);
                productRepository.save(product);
            }
        }else{
            long applicable_id=offer.getOfferCategoryId();
            Category category= categoryService.findById(applicable_id);
            List<Product> productList = productService.findProductsByCategory(category.getId());
            for(Product product : productList){
                if(product !=null) {
                    product.setSalePrice(0);
                    productRepository.save(product);
                }
            }
        }
        offerRepository.deleteById(offer.getId());

    }


    public List<OfferDto> transfer(List<Offer> offerList){
        List<OfferDto> offerDtoList=new ArrayList<>();
        for(Offer offer : offerList) {
            OfferDto offerDto=new OfferDto();
            offerDto.setId(offer.getId());
            offerDto.setName(offer.getName());
            offerDto.setDescription(offer.getDescription());
            offerDto.setOffPercentage(offer.getOffPercentage());
            offerDto.setOfferType(offer.getOfferType());
            if(offer.getOfferType().equals("Product")){
                offerDto.setOfferProductId(offer.getOfferProductId());
                offerDto.setApplicableForProductName(offer.getApplicableForProductName());
            }else{
                offerDto.setOfferCategoryId(offer.getOfferCategoryId());
                offerDto.setApplicableForCategoryName(offer.getApplicableForCategoryName());
            }
            offerDto.setEnabled(offer.isEnabled());
            offerDtoList.add(offerDto);
        }
        return offerDtoList;
    }
}
