create region --name=Product --type=PARTITION


```graphql
query {
    products(count: 10, offset: 0) {
        name
    }
}
```

```graphql
query {
    queryProducts(query: "id like 'sku-peanut%'") {
        name
        
    }
}
```


```graphql
mutation SaveProduct ($id: String!, $name: String!, $price: Float, $details: String, $ingredients: String, $directions: String, $warnings: String,
    $quantityAmount: String, $totalFatAmount: Int,
    $cholesterol: Int, $sodium: Int,
    $totalCarbohydrate: Int, $sugars: Int, $protein: Int,
    $calories: Int) {
    saveProduct(id: $id,
        name: $name,
        price: $price,
        details : $details,
        ingredients : $ingredients,
        directions : $directions,
        warnings : $warnings,
        quantityAmount : $quantityAmount,
        totalFatAmount: $totalFatAmount,
        cholesterol: $cholesterol,
        sodium: $sodium,
        totalCarbohydrate: $totalCarbohydrate,
        sugars: $sugars,
        protein: $protein,
        calories: $calories) {
        id : id,
        name : name,
        price : price,
        details : details,
        ingredients : ingredients,
        directions : directions,
        warnings : warnings,
        quantityAmount : quantityAmount
    }
}

```

Variables

```graphql
{
    "id" : "sku-peanut-butter",
    "name" : "Peanut Butter",
    "price" : 3.3,
    "details" : "details",
    "ingredients" : "ingredients",
    "directions" : "directions",
    "warnings" : "warnings",
    "quantityAmount" : "quantityAmount",
    "totalFatAmount": 3,
    "cholesterol": 3,
    "sodium": 3,
    "totalCarbohydrate": 3,
    "sugars": 3,
    "protein": 3,
    "calories": 3
}
```