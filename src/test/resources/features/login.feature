# language: en
Feature: Login in the aplication

Scenario: Login with an users
Given an user
When I login with the username "Id5" and password "123456"
Then I can access to the aplication
