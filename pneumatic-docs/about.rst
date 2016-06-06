
Background (you can skip this)
==============================

I created Pneumatic because I wanted to see if I could. I had the idea some years ago. Then I attended a conference where companies where showing their cloud integration solutions. That renewed my interested and so I finally tried out my idea.

I thought I could create a simple model for creating ETL and structured IO solutions. I knew I didn't have the skill to replicate some of the scalability features of the best modern ETL tools. I did, however, think I could create a platform with simple elements most people could understand and use to provide powerful and useful data integrations.

I did not anticipate the impact of my decision to use Spring Framework to build Pneumatic. I have used Spring a lot and it just made sense. Well, that decision has paid off over and over. Spring enabled the creation of RESTful interactions based on some very simple rules. It enabled a domain specific language for ETL, which is expressive, though not as good as a GUI. It enabled some unexpected (but obvious in retrospect) extensibility. It made creating tests, in Java and in the DSL, natural. It made creating a microservice incredibly easy.

Pneumatic doesn't compete with those enterprise-scale ETL tools on the market. Pneumatic is about simple and powerful integrations running on their own, as a service, as part of a larger program. Pneumatic is pretty light weight and can run in a container like Tomcat or from Spring Boot. I think that's awesome and hope you will find it useful.