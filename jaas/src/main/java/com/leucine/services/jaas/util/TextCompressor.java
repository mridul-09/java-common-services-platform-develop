package com.leucine.services.jaas.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;


public class TextCompressor {
  public static String compress(String textToCompress) {
    if (Utility.isEmpty(textToCompress)) {
      return textToCompress;
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      GZIPOutputStream gzip = new GZIPOutputStream(baos);
      gzip.write(textToCompress.getBytes(StandardCharsets.UTF_8));
      gzip.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Base64.getEncoder().encodeToString(baos.toByteArray());

  }


  public static void main(String[] args) throws IOException {
    String str = """
      {
        "result": [
          {
            "message": "Hello, Edward! Your order number is: #36",
            "phoneNumber": "1-959-763-9974 x62833",
            "phoneVariation": "+90 323 659 10 93",
            "status": "active",
            "name": {
              "first": "Lester",
              "middle": "Rory",
              "last": "Hettinger"
            },
            "username": "Lester-Hettinger",
            "password": "3jowwZ2ekx6Uz8V",
            "emails": [
              "Eleanora97@gmail.com",
              "Delaney20@example.com"
            ],
            "location": {
              "street": "9846 Hill Street",
              "city": "Fadelborough",
              "state": "Delaware",
              "country": "Andorra",
              "zip": "72912-1753",
              "coordinates": {
                "latitude": "-17.4726",
                "longitude": "162.9976"
              }
            },
            "website": "https://neat-guard.name/",
            "domain": "impartial-lending.net",
            "job": {
              "title": "Central Creative Developer",
              "descriptor": "Lead",
              "area": "Accounts",
              "type": "Director",
              "company": "Kunde - Pagac"
            },
            "creditCard": {
              "number": "3529-2493-1235-9838",
              "cvv": "218",
              "issuer": "visa"
            },
            "uuid": "7272945f-12d4-49ba-adf2-e7945ccfc92b",
            "objectId": "670400cb3991006f9da92a76"
          },
          {
            "message": "Hello, Jaquan! Your order number is: #77",
            "phoneNumber": "1-458-913-0494",
            "phoneVariation": "+90 368 197 10 23",
            "status": "active",
            "name": {
              "first": "Alf",
              "middle": "Billie",
              "last": "Ritchie"
            },
            "username": "Alf-Ritchie",
            "password": "C6CCMb7vGWihh82",
            "emails": [
              "Wilford.Mraz-Okuneva75@gmail.com",
              "Joaquin2@example.com"
            ],
            "location": {
              "street": "564 D'angelo Extensions",
              "city": "South Talontown",
              "state": "Connecticut",
              "country": "Republic of Korea",
              "zip": "40185",
              "coordinates": {
                "latitude": "-89.804",
                "longitude": "23.9905"
              }
            },
            "website": "https://rubbery-consciousness.info/",
            "domain": "demanding-love.com",
            "job": {
              "title": "Customer Quality Agent",
              "descriptor": "Human",
              "area": "Creative",
              "type": "Officer",
              "company": "Jakubowski - Marks"
            },
            "creditCard": {
              "number": "4392405828820",
              "cvv": "088",
              "issuer": "discover"
            },
            "uuid": "c0403df7-3d95-47f0-a417-47731517802f",
            "objectId": "670400cb3991006f9da92a77"
          },
          {
            "message": "Hello, Brandi! Your order number is: #76",
            "phoneNumber": "1-369-487-7803 x8484",
            "phoneVariation": "+90 336 610 10 53",
            "status": "active",
            "name": {
              "first": "Deon",
              "middle": "Nico",
              "last": "Hand"
            },
            "username": "Deon-Hand",
            "password": "Ch4fyRuAO1CERmC",
            "emails": [
              "Vernie_Price@gmail.com",
              "Richmond.Skiles@gmail.com"
            ],
            "location": {
              "street": "62264 Baker Street",
              "city": "Labadieshire",
              "state": "Kansas",
              "country": "Niger",
              "zip": "31948",
              "coordinates": {
                "latitude": "28.1994",
                "longitude": "-15.8661"
              }
            },
            "website": "https://jittery-cymbal.net/",
            "domain": "defiant-mixer.name",
            "job": {
              "title": "Senior Assurance Designer",
              "descriptor": "Corporate",
              "area": "Metrics",
              "type": "Agent",
              "company": "Johnson - Kemmer"
            },
            "creditCard": {
              "number": "3566-4781-3061-8414",
              "cvv": "828",
              "issuer": "diners_club"
            },
            "uuid": "4218bb00-f7a2-4c7a-ada0-b7d28be04cee",
            "objectId": "670400cb3991006f9da92a78"
          },
          {
            "message": "Hello, Hayley! Your order number is: #84",
            "phoneNumber": "546.964.2991 x39125",
            "phoneVariation": "+90 308 323 10 68",
            "status": "disabled",
            "name": {
              "first": "Lourdes",
              "middle": "Shiloh",
              "last": "Turner"
            },
            "username": "Lourdes-Turner",
            "password": "eN828PCjMTkNi8A",
            "emails": [
              "Elvis22@example.com",
              "Lois.Koch25@example.com"
            ],
            "location": {
              "street": "659 Piper Harbors",
              "city": "North Margieville",
              "state": "Maryland",
              "country": "Tonga",
              "zip": "38007",
              "coordinates": {
                "latitude": "-69.199",
                "longitude": "-87.7156"
              }
            },
            "website": "https://guilty-boost.name/",
            "domain": "shameless-bathhouse.biz",
            "job": {
              "title": "National Interactions Agent",
              "descriptor": "Product",
              "area": "Metrics",
              "type": "Representative",
              "company": "Kemmer, Orn and O'Kon"
            },
            "creditCard": {
              "number": "3529-1921-3286-2419",
              "cvv": "286",
              "issuer": "mastercard"
            },
            "uuid": "0b43a119-f56b-4791-8403-490dea43ece6",
            "objectId": "670400cb3991006f9da92a79"
          },
          {
            "message": "Hello, Edmond! Your order number is: #89",
            "phoneNumber": "862.582.8444 x06624",
            "phoneVariation": "+90 345 977 10 53",
            "status": "active",
            "name": {
              "first": "Nicklaus",
              "middle": "Kai",
              "last": "Ryan"
            },
            "username": "Nicklaus-Ryan",
            "password": "b7c0JjDqnr3AOsS",
            "emails": [
              "Colten_Waelchi@gmail.com",
              "Hester45@example.com"
            ],
            "location": {
              "street": "773 Green Ferry",
              "city": "DeKalb",
              "state": "Indiana",
              "country": "Ghana",
              "zip": "75356",
              "coordinates": {
                "latitude": "-58.1497",
                "longitude": "178.8427"
              }
            },
            "website": "https://realistic-bronco.org",
            "domain": "minty-herbs.name",
            "job": {
              "title": "Regional Intranet Planner",
              "descriptor": "Direct",
              "area": "Usability",
              "type": "Executive",
              "company": "Boyer - Gerlach"
            },
            "creditCard": {
              "number": "3422-958261-16660",
              "cvv": "855",
              "issuer": "visa"
            },
            "uuid": "33af90c9-1aa5-4381-96e9-808c7f24f828",
            "objectId": "670400cb3991006f9da92a7a"
          },
          {
            "message": "Hello, Catherine! Your order number is: #43",
            "phoneNumber": "475.695.2079 x47160",
            "phoneVariation": "+90 367 883 10 48",
            "status": "active",
            "name": {
              "first": "Sydni",
              "middle": "Drew",
              "last": "Bernhard"
            },
            "username": "Sydni-Bernhard",
            "password": "t7puslKhkiZwzwW",
            "emails": [
              "Maxie.Luettgen62@example.com",
              "Rosario.Gulgowski@gmail.com"
            ],
            "location": {
              "street": "6487 Hillside",
              "city": "Port Albertport",
              "state": "Iowa",
              "country": "American Samoa",
              "zip": "03514-5514",
              "coordinates": {
                "latitude": "-32.496",
                "longitude": "-160.9443"
              }
            },
            "website": "https://tiny-spectacles.biz",
            "domain": "unhealthy-peer-to-peer.biz",
            "job": {
              "title": "Investor Directives Designer",
              "descriptor": "Investor",
              "area": "Optimization",
              "type": "Facilitator",
              "company": "Jacobson, Gorczany and Marks"
            },
            "creditCard": {
              "number": "3755-811670-21332",
              "cvv": "130",
              "issuer": "jcb"
            },
            "uuid": "39fc4eb4-3419-42c4-be61-d9a81df8a17c",
            "objectId": "670400cb3991006f9da92a7b"
          },
          {
            "message": "Hello, Lance! Your order number is: #10",
            "phoneNumber": "401-911-0348 x95039",
            "phoneVariation": "+90 395 624 10 76",
            "status": "active",
            "name": {
              "first": "Willis",
              "middle": "Hayden",
              "last": "Ledner"
            },
            "username": "Willis-Ledner",
            "password": "rZmQlD_XWe1AhSQ",
            "emails": [
              "Fatima.Kulas43@example.com",
              "Erin.Jones84@example.com"
            ],
            "location": {
              "street": "308 W Lake Street",
              "city": "East Melynastad",
              "state": "Iowa",
              "country": "Mauritania",
              "zip": "08656",
              "coordinates": {
                "latitude": "-10.4203",
                "longitude": "-63.3827"
              }
            },
            "website": "https://naughty-diagnosis.info/",
            "domain": "doting-hashtag.biz",
            "job": {
              "title": "Human Data Director",
              "descriptor": "Global",
              "area": "Usability",
              "type": "Engineer",
              "company": "Champlin LLC"
            },
            "creditCard": {
              "number": "3529-7460-8860-3661",
              "cvv": "829",
              "issuer": "visa"
            },
            "uuid": "6d0fecf2-c36f-4d84-8269-6a13f8efa360",
            "objectId": "670400cb3991006f9da92a7c"
          },
          {
            "message": "Hello, Evangeline! Your order number is: #18",
            "phoneNumber": "845-836-7638 x2451",
            "phoneVariation": "+90 301 137 10 45",
            "status": "disabled",
            "name": {
              "first": "Mohammad",
              "middle": "Parker",
              "last": "Trantow"
            },
            "username": "Mohammad-Trantow",
            "password": "wIxNRpQvpo_jJzd",
            "emails": [
              "Amos8@example.com",
              "Jordon98@gmail.com"
            ],
            "location": {
              "street": "30992 N Maple Street",
              "city": "Fort Dusty",
              "state": "Missouri",
              "country": "Saint Pierre and Miquelon",
              "zip": "52609-5038",
              "coordinates": {
                "latitude": "57.4509",
                "longitude": "53.1542"
              }
            },
            "website": "https://hairy-dinner.net/",
            "domain": "sarcastic-estrogen.org",
            "job": {
              "title": "National Accountability Strategist",
              "descriptor": "International",
              "area": "Communications",
              "type": "Designer",
              "company": "Hagenes and Sons"
            },
            "creditCard": {
              "number": "6762544097834612606",
              "cvv": "162",
              "issuer": "visa"
            },
            "uuid": "2b622530-cc39-4013-b2fa-734f22a6f4ee",
            "objectId": "670400cb3991006f9da92a7d"
          },
          {
            "message": "Hello, Chris! Your order number is: #5",
            "phoneNumber": "796.470.0877 x31538",
            "phoneVariation": "+90 348 315 10 40",
            "status": "active",
            "name": {
              "first": "Breanne",
              "middle": "Greer",
              "last": "Lockman"
            },
            "username": "Breanne-Lockman",
            "password": "0aGXOyM0FQpitgP",
            "emails": [
              "Margarett_Koch68@gmail.com",
              "Evangeline89@gmail.com"
            ],
            "location": {
              "street": "95819 N Oak Street",
              "city": "Maggiofield",
              "state": "Massachusetts",
              "country": "Brunei Darussalam",
              "zip": "08086-0001",
              "coordinates": {
                "latitude": "-29.1138",
                "longitude": "-77.7715"
              }
            },
            "website": "https://jumbo-equal.info/",
            "domain": "old-vogue.com",
            "job": {
              "title": "Internal Marketing Officer",
              "descriptor": "Senior",
              "area": "Applications",
              "type": "Liaison",
              "company": "Gorczany Group"
            },
            "creditCard": {
              "number": "6011-6257-1473-7437-3736",
              "cvv": "213",
              "issuer": "jcb"
            },
            "uuid": "70cb00de-de09-4216-b7d1-c9370d282c1f",
            "objectId": "670400cb3991006f9da92a7e"
          },
          {
            "message": "Hello, Ila! Your order number is: #89",
            "phoneNumber": "1-967-994-0196 x93541",
            "phoneVariation": "+90 388 168 10 45",
            "status": "active",
            "name": {
              "first": "Jerome",
              "middle": "Anderson",
              "last": "Mitchell-Mann"
            },
            "username": "Jerome-Mitchell-Mann",
            "password": "_Ypz3VgYGY28qRM",
            "emails": [
              "Abdullah.Brekke33@gmail.com",
              "Paolo79@example.com"
            ],
            "location": {
              "street": "3929 Khalil Valleys",
              "city": "Delbertfort",
              "state": "Massachusetts",
              "country": "Brazil",
              "zip": "08630-2104",
              "coordinates": {
                "latitude": "31.2376",
                "longitude": "46.9481"
              }
            },
            "website": "https://bare-inspection.net",
            "domain": "skeletal-tuna.com",
            "job": {
              "title": "Global Applications Strategist",
              "descriptor": "Central",
              "area": "Paradigm",
              "type": "Liaison",
              "company": "Lesch Group"
            },
            "creditCard": {
              "number": "3445-872739-83644",
              "cvv": "558",
              "issuer": "discover"
            },
            "uuid": "0a787ace-e229-4f59-b00e-a3a9a99451ea",
            "objectId": "670400cb3991006f9da92a7f"
          },
          {
            "message": "Hello, Julie! Your order number is: #55",
            "phoneNumber": "1-233-635-8910",
            "phoneVariation": "+90 383 418 10 35",
            "status": "disabled",
            "name": {
              "first": "Zoie",
              "middle": "Robin",
              "last": "Reichel"
            },
            "username": "Zoie-Reichel",
            "password": "1aDXbsF0oLE8kGU",
            "emails": [
              "Afton.Miller@gmail.com",
              "Dolores_Kuhic12@example.com"
            ],
            "location": {
              "street": "2157 Jacobi Extension",
              "city": "Purdyfield",
              "state": "Arkansas",
              "country": "Anguilla",
              "zip": "01883-9737",
              "coordinates": {
                "latitude": "6.3003",
                "longitude": "-105.2087"
              }
            },
            "website": "https://necessary-traveler.name",
            "domain": "innocent-arch-rival.name",
            "job": {
              "title": "Global Usability Executive",
              "descriptor": "Lead",
              "area": "Response",
              "type": "Associate",
              "company": "Weber - Robel"
            },
            "creditCard": {
              "number": "5038-7704-5119-0899",
              "cvv": "804",
              "issuer": "jcb"
            },
            "uuid": "eec8b903-d48a-417e-94b4-eddb4d08c283",
            "objectId": "670400cb3991006f9da92a80"
          },
          {
            "message": "Hello, Maximillian! Your order number is: #77",
            "phoneNumber": "817.564.5189",
            "phoneVariation": "+90 357 514 10 91",
            "status": "disabled",
            "name": {
              "first": "Jaclyn",
              "middle": "Reese",
              "last": "Marks"
            },
            "username": "Jaclyn-Marks",
            "password": "6JXnIKvGYZ9Mw1F",
            "emails": [
              "Dedrick_Predovic@gmail.com",
              "Daphney_Altenwerth@example.com"
            ],
            "location": {
              "street": "157 Anika Meadow",
              "city": "Ferrytown",
              "state": "Delaware",
              "country": "Barbados",
              "zip": "07777-1045",
              "coordinates": {
                "latitude": "6.7176",
                "longitude": "-169.6493"
              }
            },
            "website": "https://royal-packaging.org",
            "domain": "red-lambkin.name",
            "job": {
              "title": "Human Communications Producer",
              "descriptor": "Future",
              "area": "Accountability",
              "type": "Producer",
              "company": "Conroy, Koss and Hudson"
            },
            "creditCard": {
              "number": "3499-235984-57797",
              "cvv": "430",
              "issuer": "maestro"
            },
            "uuid": "5f825940-e13a-4bf4-a56e-c0f65ad70fa0",
            "objectId": "670400cb3991006f9da92a81"
          },
          {
            "message": "Hello, Earline! Your order number is: #28",
            "phoneNumber": "(972) 896-6170 x34073",
            "phoneVariation": "+90 310 211 10 31",
            "status": "disabled",
            "name": {
              "first": "Davin",
              "middle": "Bowie",
              "last": "Hegmann"
            },
            "username": "Davin-Hegmann",
            "password": "XEpCWS0qpmVQE6W",
            "emails": [
              "Evelyn79@gmail.com",
              "Leopoldo_Nolan99@gmail.com"
            ],
            "location": {
              "street": "123 Walsh Curve",
              "city": "Rowland Heights",
              "state": "Hawaii",
              "country": "Nauru",
              "zip": "34467",
              "coordinates": {
                "latitude": "22.8493",
                "longitude": "-175.9041"
              }
            },
            "website": "https://soupy-load.com/",
            "domain": "well-to-do-polenta.com",
            "job": {
              "title": "Product Research Producer",
              "descriptor": "Chief",
              "area": "Group",
              "type": "Liaison",
              "company": "Dickinson LLC"
            },
            "creditCard": {
              "number": "5415-0024-7599-0150",
              "cvv": "933",
              "issuer": "discover"
            },
            "uuid": "3733468a-74f3-48d6-acba-85bb0d3788fa",
            "objectId": "670400cb3991006f9da92a82"
          },
          {
            "message": "Hello, Daphne! Your order number is: #89",
            "phoneNumber": "555.840.0604 x02086",
            "phoneVariation": "+90 393 658 10 60",
            "status": "disabled",
            "name": {
              "first": "Houston",
              "middle": "Nico",
              "last": "Dibbert"
            },
            "username": "Houston-Dibbert",
            "password": "up4qiWaGftGABAC",
            "emails": [
              "Hertha91@gmail.com",
              "Madyson_Lang@gmail.com"
            ],
            "location": {
              "street": "37358 Roob Park",
              "city": "O'Harafield",
              "state": "New Jersey",
              "country": "Brunei Darussalam",
              "zip": "42393-4985",
              "coordinates": {
                "latitude": "-71.8387",
                "longitude": "68.4269"
              }
            },
            "website": "https://crowded-spell.org",
            "domain": "dutiful-exhibition.net",
            "job": {
              "title": "Dynamic Marketing Liaison",
              "descriptor": "Product",
              "area": "Mobility",
              "type": "Agent",
              "company": "Kulas Inc"
            },
            "creditCard": {
              "number": "3001-680458-9133",
              "cvv": "900",
              "issuer": "discover"
            },
            "uuid": "a2edf395-b7dc-4d2b-a13c-56da48a123cd",
            "objectId": "670400cb3991006f9da92a83"
          },
          {
            "message": "Hello, Leonardo! Your order number is: #11",
            "phoneNumber": "1-346-763-1250",
            "phoneVariation": "+90 390 533 10 34",
            "status": "active",
            "name": {
              "first": "Jessy",
              "middle": "Kai",
              "last": "Corwin"
            },
            "username": "Jessy-Corwin",
            "password": "wT7PQ3xuKgTgui8",
            "emails": [
              "Morgan_Crist@gmail.com",
              "Cale52@example.com"
            ],
            "location": {
              "street": "1569 Javonte Fall",
              "city": "Asheville",
              "state": "New Jersey",
              "country": "Poland",
              "zip": "98576",
              "coordinates": {
                "latitude": "17.0553",
                "longitude": "44.0674"
              }
            },
            "website": "https://puzzling-outrun.net",
            "domain": "shadowy-sneakers.net",
            "job": {
              "title": "Corporate Group Strategist",
              "descriptor": "Central",
              "area": "Creative",
              "type": "Manager",
              "company": "Fay - Gerlach"
            },
            "creditCard": {
              "number": "589338754711929627",
              "cvv": "126",
              "issuer": "american_express"
            },
            "uuid": "99883e59-a59f-4bf3-bcd1-0c7ff1a48a51",
            "objectId": "670400cb3991006f9da92a84"
          },
          {
            "message": "Hello, Waylon! Your order number is: #8",
            "phoneNumber": "249-959-3606 x3881",
            "phoneVariation": "+90 310 204 10 41",
            "status": "disabled",
            "name": {
              "first": "Jamison",
              "middle": "Finley",
              "last": "Lesch"
            },
            "username": "Jamison-Lesch",
            "password": "ZFdXv_JNzuaMpie",
            "emails": [
              "Benjamin.Bins35@gmail.com",
              "Bertha.Bergnaum51@gmail.com"
            ],
            "location": {
              "street": "4581 N Pearl Street",
              "city": "Altadena",
              "state": "Wyoming",
              "country": "Micronesia",
              "zip": "73317-0580",
              "coordinates": {
                "latitude": "-29.6887",
                "longitude": "-155.3745"
              }
            },
            "website": "https://rapid-mining.com/",
            "domain": "obedient-stress.biz",
            "job": {
              "title": "Principal Research Associate",
              "descriptor": "Internal",
              "area": "Research",
              "type": "Agent",
              "company": "Purdy, Flatley and Stanton"
            },
            "creditCard": {
              "number": "5018-4399-4357-6404",
              "cvv": "580",
              "issuer": "jcb"
            },
            "uuid": "df29aba5-4655-40db-9e3c-f54a42fa24bb",
            "objectId": "670400cb3991006f9da92a85"
          }
        ]
      }
      """;
    System.out.println("BEFORE: " + str.length());
    System.out.println("COMPRESS: " + compress(str));

  }


}
// Method to decompress Brotli compressed data}
