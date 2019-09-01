Feature: Token generation fo rider

  Scenario: Generate token for rider
    Given url 'https://auth.staging.arctern.co/oauth/token'
    And request "username=9999999999&password=111111&grant_type=password"
    And header Content-Type = 'application/x-www-form-urlencoded'
    And header Authorization = 'Basic TFJMNlpDRXQ1QWc3d2hhYTpMd3BycmpkTHpNYzl6U3E0WFk0aldDejNoTldZTVZWNg=='
    When method POST
    Then status 200
    And def accessToken = response.access_token

