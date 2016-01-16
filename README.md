# Detangler

Analyzes dependency structure and cycles

# Levels
- there must be at least 1 level
- the last level is always the class level
- the second to last level is the package level
- the first x levels greater than 2 establish a hierarchy
- every class will be broken down into the same number of levels, creating placeholders if necessary

# 3 level example

Suppose you had the following configuration

    {
      level 3
      startsWith {
        include [[com seanshubin]]
        drop [[com seanshubin]]
      }
    }

And the following (contrived) classes

    com.seanshubin.detangler.analysis.Detangler
    com.seanshubin.detangler.analysis.tree.DetanglerBackedByTree
    com.seanshubin.detangler.Metrics
    com.seanshubin.CollectionUtil

Here is how they would be broken down into levels

| drop           | top       | package           | class                 |
| -------------- | --------- | ----------------- | --------------------- |
| com.seanshubin | detangler | analysis          | Detangler             |
| com.seanshubin | detangler | analysis.tree     | DetanglerBackedByTree |
| com.seanshubin | detangler | root-of-detangler | Metrics               |
| com.seanshubin | -root-    | -root-            | CollectionUtil        |

