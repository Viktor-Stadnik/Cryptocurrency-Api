# Cryptocurrency-Api :bar_chart: :currency_exchange: <a id="anchor"></a>
### About Project:
The project is to get the information for cryptocurrencies from a web-site [CEX.IO](https://cex.io/rest-api),
save this data in a database, and manage them. 
The application should get last prices in USD for following cryptocurrencies pairs: BTC, ETH, XRP each 10 seconds.
- [X] Create a cron job timer :clock1: that runs every 10 seconds.
- [X] Create a rest controller with the following endpoints:
- `[GET]  /cryptocurrencies/minprice?name=[currency_name]` - 	:chart_with_upwards_trend: should return record with the lowest price of selected cryptocurrency.
- `[GET]  /cryptocurrencies/maxprice?name=[currency_name]` - :chart_with_downwards_trend: should return record with the highest price of selected cryptocurrency [currency_name] possible values: BTC, ETH or XRP. If some other value is provided then appropriate error message should be thrown.
- `[GET]  /cryptocurrencies?name=[currency_name]&page=[page_number]&size=[page_size]` - :bookmark_tabs: should return a selected page with selected number of elements and default sorting should be by price from lowest to highest. For example, if *page=0&size=10*, then you should return first 10 elements from database, sorted by price from lowest to highest. *[page_number]* and *[page_size]* request parameters should be optional, so if they are missing then you should set them default values page=0, size=10.
- [X] Create an endpoint that will generate a CSV report saved into file:
- `[GET]  /cryptocurrencies/csv` - :card_index: the report should contain the following fields: Cryptocurrency Name, Min Price, Max Price. So there should be only three records in that report, because we have three different cryptocurrencies.
____
### Technologies have been used:
- Java 11
- MongoDB
- String Boot: Data, Web
- REST
- Maven, Lombok, OpenCSV, Testcontainers, log4j
- JUnit, Mockito
____
### Description:
- Each cryptocurrencies in DB are presented in this form: id, marketSymbol, price and createdAt - the date of creation
- After launching the application, you will see a message in the console that the system is updated every 10 seconds
- For getting information from BD, connect to ***localhost:8080***
- Use ***/cryptocurrencies/minprice?name=ETH*** - get the lowest price for this currency
- Use ***/cryptocurrencies/maxprice?name=XRP*** - get the highest price for this currency
- Use ***/cryptocurrencies?name=BTC*** - get all cryptocurrencies from lowest to highest prices
It is also possible to change the display of the number of currencies on the page, the page itself and the sorting direction, for example:
    -  /cryptocurrencies?name=BTC&page=0&size=20&sortBy=price:DESC
- Use ***/cryptocurrencies/csv*** - get the csv file with a currency report that contains the maximum and minimum prices
____
### How to run this application:
1. Fork this project
2. Install MongoDB and create a collection
3. Write your credentials in the `resources/application.properties` file
4. Install Docker (run testcontainers
5. Testing project using command: mvn test

	:arrow_up: [Up](#anchor)
