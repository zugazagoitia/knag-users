<h1 align="center">
  <br>
  <a href="https://github.com/zugazagoitia/knag">
    <img src="https://raw.githubusercontent.com/zugazagoitia/knag/main/icons/logo@0.25x.png" alt="Knag logo" width="100">
  </a>
  <br>
  Knag
  <br>
</h1>

<h4 align="center">An Algorithmic Trading SaaS platform for my TFG (Final Degree Project)</h4>

<h3 align="center">
  Users Service
</h3>

<p align="center">
  <a href="https://github.com/zugazagoitia/knag-users/actions/workflows/actions.yml?query=branch%3Amain">
    <img src="https://img.shields.io/github/workflow/status/zugazagoitia/knag-users/build/main?label=build%20%28main%29&logo=githubactions&logoColor=%23FFFFFF" alt="main build badge">
  </a>
  <a href="https://github.com/zugazagoitia/knag-users/actions/workflows/actions.yml?query=branch%3Adev">
    <img src="https://img.shields.io/github/workflow/status/zugazagoitia/knag-users/build/dev?label=build%20%28dev%29&logo=githubactions&logoColor=%23FFFFFF" alt="dev build badge">
  </a>
  <a href="https://github.com/zugazagoitia/knag-users/releases">
    <img alt="GitHub release (latest SemVer)" src="https://img.shields.io/github/v/release/zugazagoitia/knag-users?sort=semver">
  </a>
</p>

##Requirements

In order to run the service the following software/APIs are required

- A [MongoDB](https://www.mongodb.com/try/download) database
- The [Mailgun](https://www.mailgun.com/) API for email verification
- The [reCAPTCHA v2](https://developers.google.com/recaptcha/intro) API for the registration captcha


##Usage

Working images are published to the GitHub Packages repository attached to this repository, they are also published under [releases](https://github.com/zugazagoitia/knag-users/releases).

##Configuration

The following environment variables need to be populated in order for the service to work correctly.

| Name                                        | Description                                                          |    Constraints |
|:--------------------------------------------|----------------------------------------------------------------------|---------------:|
| SPRING_DATA_MONGODB_HOST                    | A MongoDB host                                                       |              - |
| SPRING_DATA_MONGODB_PORT                    | The MongoDB port                                                     |              - |
| SPRING_DATA_MONGODB_AUTHENTICATION-DATABASE | The Authentication database to use for login                         |              - |                                    
| SPRING_DATA_MONGODB_DATABASE                | MongoDB database to use                                              |              - |
| SPRING_DATA_MONGODB_USERNAME                | A MongoDB username authorized on the previous database               |              - |                     
| SPRING_DATA_MONGODB_PASSWORD                | The password for the user                                            |              - |   
| MAILGUN_APIURL                              | The Mailgun api endpoint to use (e.g. https://api.eu.mailgun.net/v3) |              - |
| MAILGUN_APIKEY                              | The Mailgun api key                                                  |              - |
| MAILGUN_DOMAIN                              | The Mailgun domain to use                                            |              - |
| MAILGUN_SENDER                              | The email address to use for sending                                 |              - |
| RECAPTCHA_KEY_SITE                          | A reCAPTCHA v2 sitekey                                               |              - |
| RECAPTCHA_KEY_SECRET                        | The matching reCAPTCHA v2 secret for the used sitekey                |              - |
| KNAG_KEY_PRIV                               | Private key used for signing JWTs                                    | PKCS #8 Syntax |
| KNAG_KEY_PUB                                | Public key used for verifying JWTs                                   |   X.509 Syntax |

 ##Endpoints

The service is exposed on the port `8080`

There are kubernetes probes exposed on the port `3000`

