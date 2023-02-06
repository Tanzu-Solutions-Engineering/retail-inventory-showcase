create region --name=Product --type=PARTITION


```graphql
query {
    products(count: 10, offset: 0) {
        name
    }
}
```


```graphql
mutation SaveProduct ($id: String!, $name: String!) {
    saveProduct(id: $id, name: $name) {
        id : id,
        name : name
    }
}
```

Variables

```graphql
{
  "id":"hello",
  "name" : "world"
}
```