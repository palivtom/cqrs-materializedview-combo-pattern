rootProject.name = "bachelor-example-library"

includeBuild("order-service")
includeBuild("order-updater-service")
includeBuild("order-view-service")

includeBuild("transaction-grouper-service")

includeBuild("utils/security")
includeBuild("utils/order-view-model")
includeBuild("utils/transaction-grouper-model")
includeBuild("utils/updater-model")
