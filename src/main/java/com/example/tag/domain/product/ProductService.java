package com.example.tag.domain.product;

import com.example.tag.domain.product.entity.Product;
import com.example.tag.domain.product.repository.ProductRepository;
import com.example.tag.domain.submission.entity.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAvailableProducts() {
        return productRepository.findByStatus("AVAILABLE");
    }

    @Transactional
    public void createProductFromSubmission(Submission submission) {
        Product product = Product.builder()
                .submission(submission) // 접수 정보 연결 (판매자 정보는 submission 안에)
                .title("접수된 상품 (" + submission.getType() + ")")
                .price(submission.getPriceRequest())    // 희망 가격
                .imageUrl(submission.getProofImageUrl())    // 이미지
                .status("AVAILABLE")    // 바로 판매 시작 상태로 설정
                // category, size, conditionStatus = default null
                .build();

        productRepository.save(product);
    }
}