package com.example.Inventory.services;

import com.example.Inventory.elastic.ProductRepository;
import com.example.Inventory.models.Product;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class ProductFun {

    @Autowired
    private MongoTemplate mongoTemplate;
    private ProductRepository productRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /*public List<Product> getQueryProductsRegex(String prodName) {
        Criteria regex = Criteria.where("prodName").regex(prodName, "i");
        Query query = new Query();
        query.addCriteria(regex);
        return mongoTemplate.find(query, Product.class);
    }*/

    public String saveProduct(Product product) throws IOException {
        IndexResponse response = elasticsearchTemplate.getClient().prepareIndex("elasticsearch", "product", product.getProdId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("prodId", product.getProdId())
                        .field("price", product.getPrice())
                        .field("prodName", product.getProdName())
                        .field("description", product.getDescription())
                        .endObject()
                ).get();
        return response.getId();
    }

    public Product findProductById(String productId) {
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch("elasticsearch")
                .setTypes("product")
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("prodId", productId))
                .get();
        SearchHit[] searchHits = response.getHits().getHits();
        Map<String, Object> map = searchHits[0].getSourceAsMap();
        return new Product(map.get("prodId").toString(),map.get("prodName").toString(),(int)map.get("price"),map.get("description").toString()) ;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findProductByNameAndDescription(String description) {
        QueryBuilder query = QueryBuilders.boolQuery()
                .should(
                        QueryBuilders.queryStringQuery(description)
                                .lenient(true)
                                .field("prodName")
                                .field("description")
                )
                .should(
                        QueryBuilders.queryStringQuery("*" + description + "*")
                                .lenient(true)
                                .field("prodName")
                                .field("description")
                );
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
        return elasticsearchTemplate.queryForList(nativeSearchQuery, Product.class);
    }

    public List<Product> search(String keywords) {
        MatchPhrasePrefixQueryBuilder searchByKeywords =
                QueryBuilders.matchPhrasePrefixQuery("prodName", keywords);
        Iterable<Product> products = productRepository.search(searchByKeywords);
        List<Product> list = new ArrayList<>();
        for (Product p : products) {
            list.add(p);
        }
        /*        MatchQueryBuilder searchByKeywords = QueryBuilders.matchQuery("prodName", keywords);
                List<Product> products = productRepository.search(searchByKeywords);
                System.out.println(products.size() + " is prodSize");
                return products;*/
        return list;
    }
}

