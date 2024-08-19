package io.spring.retail.product.service;

import com.vmware.retail.product.domain.Nutrition;
import com.vmware.retail.product.domain.Product;
import io.spring.retail.product.repository.ProductGemFireRepository;
import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.util.Digits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class ProductServiceAppTests {

	@Autowired
	ProductGemFireRepository repository;

	@Test
	void contextLoads() {
	}


	@Test
	void loadData() throws IOException {

		String path = "/Users/Projects/solutions/cloudNativeData/showCase/dev/retail-store-cloud-native-data/applications/greenPlum/product.csv";

		File file = new File(path);

		//4,PBR,1,1004,,,,,,,

		var reader = new CsvReader(file);

		final Digits[] digit = {new Digits()};

		final Nutrition[] nutrition = new Nutrition[1];
		final int[] i = {1};
		reader.forEach( row -> {
			var product = Product.builder()
					.id("sku-"+row.get(0))
					.name(row.get(1))
					.details(row.get(1))
					.ingredients("ingredients "+ i[0])
					.directions("directions "+i[0])
					.price(digit[0].generateDouble(5.5,9.5))
					.quantityAmount("5G")
					.warnings("warnings "+i[0])
					.nutrition(Nutrition.builder()
							.calories(digit[0].generateInteger(23,330))
							.cholesterol(digit[0].generateInteger(0,200))
							.protein(digit[0].generateInteger(0,25))
							.sodium(digit[0].generateInteger(0,500))
							.totalCarbohydrate(digit[0].generateInteger(0,230))
							.totalFatAmount(digit[0].generateInteger(0,400))
							.build()).build();

			repository.save(product);

			i[0]++;
		});



	}
}
