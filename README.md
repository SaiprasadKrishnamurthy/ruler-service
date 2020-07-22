# A Simple/Lightweight/Cloud Native Rule Engine as a Service.

## Fundamentals:

### Rule Engine:

### Rules:

### RuleSet

### Rule Types:

#### Truth Tables:

| Primary Rule | Alternate Rule | Final Result |
|--------------|----------------|--------------|
| True         | Not Executed   | True         |
| False        | True           | True         |
| False        | False          | False        |

| Primary Rule | Override Rule | Final Result                               |
|--------------|---------------|--------------------------------------------|
| True         | True          | True (Overridden Rule Content is chosen)   |
| False        | True          | True (Overridden Rule Content is chosen)   |
| False        | False         | False                                      |
| True         | False         | False (Primary Rule Content is not chosen) |