# Spring Boot + Spring Security + React + Bootstrap = Starter Kit

This repository contains a starter kit for a Java (I use Java 11) application based on Spring Boot (2.1.1), which uses React (16.7). Additionally, there's Spring Security integrated and fine-tuned so that it supports `application/json` login. Oh, there's also `reactstrap` in there for you to quickly get your next super app running!

## Java
The project was generated from Spring Initializer. There's a few new classes in the `pl.pateman.springbootreactbootstrapauthstarter.security` package that make `application/json` logins possible (apparently, Spring Security works only with standard forms). I used my favorite GSON for JSON serialization. I also had to tweak the remember-me mechanism, because the default implementation didn't want to pick up the parameter for the very same reason - I don't send a plain form.

We have the `JSONAuthenticationFilter`, which is responsible for parsing the request and creating a `JSONAuthenticationToken` - it's an equivalent of Spring's `UsernamePasswordAuthenticationToken` that stores the current user.

There's the `JSONLoginHandler` that returns appropriate responses to the client, depending on whether the authentication has been successful or not.

Another class worth mentioning is `JSONAuthenticationProvider`. It's needed, because the default Spring's provider creates a `UsernamePasswordAuthenticationToken` when authentication succeeds, which is not what I want. I want my token class to be returned, so that `JSONRememberMeServices` can use it to create the 'remember-me' cookie. Nothing too fancy going on there, just making sure that I have the correct instance in the end.

There are two different users:

* **Login**: admin, **Password**: admin, **Role**: ADMIN
* **Login**: user, **Password**: user, **Role**: USER

## React
Inside the `app` directory you can find a very basic React application with two pages - a login form and a simple dashboard page, which greets the user with their user name and a personalized message (depending on the user's role). 

The application has been bootstraped with `create-react-app`. I've included React Router, axios, and reactstrap as auxiliaries.

## Gradle
I've added a really basic task called `bootWarWithReact`, which bundles the React app inside the WAR. It basically runs `npm run build` inside the React app's directory, and copies the result to the `static` directory inside the Java app.

## Running
Just run the Java app from your favorite IDE, and navigate to the `app` directory in your terminal and run `npm start` to run the React server. 

The Java app will be available under `http://localhost:8080`, React under `http://localhost:3000`.

## Questions, concerns, improvement
Feel free to create an issue here or submit a PR if you want to make this project better. I wanted to create a starting point for everyone, so that they can focus on writing their application instead of spending time on setting it up. :)