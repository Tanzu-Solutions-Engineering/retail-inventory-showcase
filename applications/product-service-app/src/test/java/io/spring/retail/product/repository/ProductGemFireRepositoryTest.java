package io.spring.retail.product.repository;

import com.vmware.retail.product.domain.Product;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.query.SelectResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.gemfire.GemfireTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductGemFireRepositoryTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private GemfireTemplate template;
    private ProductGemFireRepository subject;
    private Product product = JavaBeanGeneratorCreator.of(Product.class).create();

    @BeforeEach
    void setUp() {
        subject = new ProductGemFireRepository(repository, template);
    }

    @Test
    void findAll() {

        Iterable<Product> expected = mock(Iterable.class);
        when(repository.findAll()).thenReturn(expected);

        var actual = subject.findAll();

        assertEquals(expected, actual);
    }



    @Test
    void given_when_save_then_when_save_then_repository_saves() {
        subject.save(product);

        verify(this.repository).save(any(Product.class));
    }


    @Test
    void given_predicate_region_where_query_then_return_results() {

        SelectResults<Object> expected = mock(SelectResults.class);
        when(template.query(anyString())).thenReturn(expected);

        Iterable<Product> actual = subject.queryProducts("details like 'D%'");
        assertEquals(expected, actual);
    }

    @Test
    void given_id_when_findById_then_Return_Product() {

        Product expected = JavaBeanGeneratorCreator.of(Product.class).create();

        when(repository.findById(anyString())).thenReturn(Optional.of(expected));
        assertEquals(expected, subject.findById(expected.getId()));
    }
}