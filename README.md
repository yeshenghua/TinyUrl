# TinyUrl
A SpringBoot project which lets user shorten their long url.
REST API has been used , MYSQL database and Redis Cache has been used for higher read throughput. A Client wise rate limiter has been introduced so that one client can only request the service maximum 5 times over a span of 2 seconds.
