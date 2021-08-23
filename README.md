Example scala service to copy for new applications


* Runs on Heroku
* CRUD via REST
* Postgres as the datasource (https://devcenter.heroku.com/articles/heroku-postgresql)

In the future:
* User management
* Database migrations -> evaluate https://flywaydb.org/

Steps to take while copying for a new app:
* [...]
* 


Local setup:
* Install postgres and set `DATABASE_URL`
  * `export DATABASE_URL=postgres://$(whoami)`
  * `sudo apt-get install postgresql`
  * To connect locally: `sudo -u postgres psql postgres`

