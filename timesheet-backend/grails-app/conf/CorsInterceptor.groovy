class CorsInterceptor {

    CorsInterceptor() {
        matchAll()
    }

    boolean before() {

        response.setHeader(
            "Access-Control-Allow-Origin",
            "*"
        )

        response.setHeader(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS"
        )

        response.setHeader(
            "Access-Control-Allow-Headers",
            "*"
        )

        return true
    }
}