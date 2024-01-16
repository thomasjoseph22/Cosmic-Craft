package com.example.library.service.impl;

import com.example.library.dto.ProductDto;
import com.example.library.model.CartItem;
import com.example.library.model.Image;
import com.example.library.model.Product;
import com.example.library.model.Wishlist;
import com.example.library.repository.CartItemRepository;
import com.example.library.repository.ImageRepository;
import com.example.library.repository.ProductRepository;
import com.example.library.repository.WishlistRepository;
import com.example.library.service.ProductService;
import com.example.library.utils.ImageUpload;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ImageRepository imageRepository;


    private ImageUpload imageUpload;
    private CartItemRepository cartItemRepository;

    private WishlistRepository wishlistRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ImageUpload imageUpload, ImageRepository imageRepository, CartItemRepository cartItemRepository, WishlistRepository wishlistRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.imageUpload = imageUpload;
        this.cartItemRepository=cartItemRepository;
        this.wishlistRepository=wishlistRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> allProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }
    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setShortDescription(product.getShortDescription());
            productDto.setLongDescription(product.getLongDescription());
            productDto.setBrand(product.getBrand());

            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public Product save(List<MultipartFile> imageProducts, ProductDto productDto) {
        Product product = new Product();
        try {

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setShortDescription(productDto.getShortDescription());
            product.setLongDescription(productDto.getLongDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_activated(true);
            Product savedProduct = productRepository.save(product);
            if (imageProducts == null) {
                product.setImage(null);
            } else {
                List<Image> imagesList = new ArrayList<>();
                for (MultipartFile imageProduct : imageProducts) {
                    Image image = new Image();
                    String imageName = imageUpload.storeFile(imageProduct);
                    image.setName(imageName);
                    image.setProduct(product);
                    imageRepository.save(image);
                    imagesList.add(image);
                }
                product.setImage(imagesList);
            }
            return savedProduct;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductDto findById(long id) {
        Product product = productRepository.findById(id);
        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setImage(product.getImage());
        productDto.setName(product.getName());
        productDto.setShortDescription(product.getShortDescription());
        productDto.setLongDescription(product.getLongDescription());
        productDto.setBrand(product.getBrand());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCategory(product.getCategory());
        productDto.setActivated(product.is_activated());
        return productDto;
    }

    @Override
    public Product update(List<MultipartFile> imageProducts, ProductDto productDto) {
        try {
            long id= productDto.getId();
            Product productUpdate = productRepository.findById(id);



            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setName(productDto.getName());
            productUpdate.setBrand(productUpdate.getBrand());
            productUpdate.setShortDescription(productDto.getShortDescription());
            productUpdate.setLongDescription(productDto.getLongDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            productRepository.save(productUpdate);
            if (imageProducts != null && !imageProducts.isEmpty() && imageProducts.size()!=1) {
                List<Image> imagesList = new ArrayList<>();
                List<Image> image = imageRepository.findImageBy(id);
                int i=0;
                for (MultipartFile imageProduct : imageProducts) {
                    String imageName = imageUpload.storeFile(imageProduct);
                    image.get(i).setName(imageName);
                    image.get(i).setProduct(productUpdate);
                    imageRepository.save(image.get(i));
                    imagesList.add(image.get(i));
                    i++;
                }
                productUpdate.setImage(imagesList);
            }

            return productUpdate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void disable(long id) {
        Product product=productRepository.findById(id);
        product.set_activated(false);

        productRepository.save(product);
    }

    @Override
    public void enable(long id) {
        Product product=productRepository.findById(id);
        product.set_activated(true);

        productRepository.save(product);

    }

    @Override
    public Page<ProductDto> findAllByActivated(long id,int pageNo) {
        List<Product> products=productRepository.findAllByActivatedTrue(id);
        List<ProductDto>productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 3);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }
    @Override
    public Page<ProductDto> findAllByActivated(int pageNo,String sort) {
        List<Product> products;

        if ("lowToHigh".equals(sort)) {
            products = productRepository.findAllByActivatedTrueAndSortBy("lowToHigh");
        } else if ("highToLow".equals(sort)) {
            products = productRepository.findAllByActivatedTrueAndSortBy("highToLow");
        } else {
            products = productRepository.findAllByActivatedTrue();
        }

        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 3);
        return toPage(productDtoList, pageable);

    }

    @Override
    public List<ProductDto> findAllProducts() {
        List<Product> products=productRepository.findAllByActivatedTrue();
        List<ProductDto>productDtoList = transferData(products);
        return productDtoList;
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    @Override
    public List<ProductDto> findAllByOrderDesc() {
        List<Product> products = productRepository.findAllByOrderById();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id);
        imageRepository.deleteImagesByProductId(id);
        Set<CartItem> cartItemSet=product.getCartItems();
        for(CartItem cartItem : cartItemSet){
            cartItem.setProduct(null);
            cartItemRepository.save(cartItem);
        }
        Wishlist wishlist=product.getWishlist();
        if(wishlist!=null) {
            wishlistRepository.delete(wishlist);
        }

        productRepository.delete(product);
    }

    @Override
    public Long countAllProducts() {
        return  productRepository.CountAllProducts();
    }

    @Override
    public List<Object[]> getProductStats() {
        return productRepository.getProductStatsForConfirmedOrders();
    }

    @Override
    public List<Object[]> getProductsStatsBetweenDates(Date startDate, Date endDate) {
        return productRepository.getProductsStatsForConfirmedOrdersBetweenDates(startDate,endDate);
    }


    @Override
    public Product findBYId(long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findProductsByCategory(long id) {
        return productRepository.findAllByCategoryId(id);
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 3);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }
}