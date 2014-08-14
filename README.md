INTRODUCTION
===========
This is a demo app meant for Ona CTO and his team. It demostrates processing of remote json data. Specifically, it uses the data at:
https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json

It's a Java web application. Therefore, to build it, you'll need Java JDK 7 and Maven 3. To run it, you'll need a servlet container like Apache Tomcat.


BUILD AND DEPLOY
===============
This project is maven-based. To build it, ensure you have Maven 3 installed, then run:

mvn clean install

This will create a web archive (.war) file, named something like ona-data-xxx.war. Rename it to ona-data.war. Dump it into a servlet container like Apache Tomcat or an application server like JBoss.

The application exposes a REST endpoint that can be invoked using the URL:

http://hostname-or-ip:port/ona-data/api/getData?url=[url-of-data]

But first, to test if the deployment was successful, invoke this endpint:
http://hostname-or-ip:port/ona-data/api/test

This should return 

{"message":"Request processing successful","responseCode":200,"data":"This is just a test. It was successful"}

if the application deployment was started successfully.

PROCESSING THE JSON FILE
========================
Now to process a json file at a specified url, invoke this endpoint with the specified parameters:

http://hostname-or-ip:port/ona-data/api/getData?url=https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json.

With the data at the above specified URL, the result should be:

{
    "message": "Request processing successful",
    "responseCode": 200,
    "data": {
        "pointsPerCommunity": {
            "Sikabsa": 3,
            "Gbima": 3,
            "Jiniensa": 1,
            "Chondema": 4,
            "Nawaasa": 6,
            "Guuta": 32,
            "Vundema": 5,
            "Bechinsa": 26,
            "Nyankpiensa": 8,
            "Jagsa": 38,
            "Zukpeni": 6,
            "Gaadem": 2,
            "Piisa": 5,
            "Zanwara": 10,
            "Banyangsa": 10,
            "Kpikpaluk": 3,
            "Tantala": 22,
            "Kanbangsa": 8,
            "Gumaryili": 1,
            "Loagri_1_": 18,
            "Jiriwiensa": 8,
            "Tankangsa": 6,
            "Zua": 28,
            "Soo": 7,
            "Nyandema": 3,
            "Bandem": 7,
            "Kpatarigu": 51,
            "Nabulugu": 31,
            "Jaata": 8,
            "Badomsa": 27,
            "Kanwaasa": 9,
            "Kom": 6,
            "Alavanyo": 3,
            "Dorinsa": 17,
            "Kalaasa": 1,
            "Dibisi": 2,
            "Zundem": 30,
            "Tuisa": 4,
            "Jiningsa-Yipaala": 3,
            "Fiisa": 5,
            "Jiningsa": 7,
            "Longsa": 9,
            "Chanpolinsa": 4,
            "Kurugu": 9,
            "Kulbugu": 11,
            "Zogsa": 6,
            "Akpari-yeri": 3,
            "Chansa": 9,
            "Gbaarigu": 5,
            "Nayoku": 35,
            "Kaasa": 25,
            "Selinvoya": 13,
            "Logvasgsa": 4,
            "Zuedema": 18,
            "Zangu-Vuga": 13,
            "Arigu": 12,
            "Mwalorinsa": 8,
            "Abanyeri": 4,
            "Namgurima": 8,
            "Guuta-Nasa": 11,
            "Kubore": 18,
            "Kunkwah": 3,
            "Suik": 1,
            "Garigu": 1,
            "Luisa": 8
        },
        "numWaterPoints": 590,
        "rankByBrokenPoints": {
            "Dorinsa": 0.058823529411764705,
            "Guuta": 0.0625,
            "Selinvoya": 0.07692307692307693,
            "Kulbugu": 0.09090909090909091,
            "Kpatarigu": 0.09803921568627451,
            "Kanwaasa": 0.1111111111111111,
            "Nabulugu": 0.12903225806451613,
            "Zua": 0.14285714285714285,
            "Zangu-Vuga": 0.15384615384615385,
            "Jagsa": 0.15789473684210525,
            "Nawaasa": 0.16666666666666666,
            "Zogsa": 0.16666666666666666,
            "Nayoku": 0.17142857142857143,
            "Longsa": 0.2222222222222222,
            "Loagri_1_": 0.2222222222222222,
            "Chanpolinsa": 0.25,
            "Tantala": 0.2727272727272727,
            "Kubore": 0.2777777777777778,
            "Banyangsa": 0.3,
            "Alavanyo": 0.3333333333333333,
            "Gbima": 0.3333333333333333,
            "Zanwara": 0.4,
            "Soo": 0.42857142857142855,
            "Bandem": 0.42857142857142855,
            "Kurugu": 0.4444444444444444,
            "Arigu": 0.5,
            "Namgurima": 0.5,
            "Gbaarigu": 0.6,
            "Zukpeni": 0.6666666666666666
        }
    }
}

JSON is the default response. However, you can also request for the data in XML format by adding 'format' query parameter. For example: 
http://hostname-or-ip:port/ona-data/api/getData?url=https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json&format=xml

Finally, you can also make JSONP requests by specifying a name of a callback function as one of the query parameters. For example:
http://hostname-or-ip:port/ona-data/api/getData?url=https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json&callback=_callbackFn

