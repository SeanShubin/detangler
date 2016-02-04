# Detangler

Mitigating architectural chaos as complexity increases

## Why use tooling?
- the problem is that complexity continually increases
- no amount of discipline, intelligence, or skill can scale arbitrarily if the tool you are using is the human brain
    - brain can only keep track of 7 things at a time, and only 2 nesting levels
    - detecting new tangles requires knowledge you will not be paying attention to
        - you are thinking about the where you are going, not how you got here
- automated feedback can narrow the relevant information to what a human brain can keep track of

## Methodology
- Quantify a problem
- Fail a build if the quantity increases
- Fix a single occurrence of the problem
- Lower the quantity that the build tolerates

## Prerequisite Concepts
- Low Coupling
- High Cohesion
- Design By Contract
- Dependency Inversion Principle
- Stable Dependencies Principle

## How to read the report
- Granularity
- Summary
- Dependency Breakdown
- Bold Circles
- Red Squares

## Find problem from graph alone

## Find problem by digging down reasons tree

