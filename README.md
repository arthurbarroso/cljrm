## cljrm

#### Base steps:
1. Clone this repository
2. `docker-compose up -d`
3. copy your schema and seed files into your container (I did this through docker cp):
```
docker cp resources/seed.sql container_id:/
docker cp resources/schema.sql container_id:/
```
4. connect to your database and run the files
```
docker exec -it cljrm-db bash
psql -U postgres
\connect cljrm
\i schema.sql
\i seed.sql
```
please also be sure to do
```
(defmethod ig/prep-key :db/postgres
  [_ config]
  (merge config {:jdbc-url (env :jdbc-url)}))
```
instead of
```
(defmethod ig/prep-key :db/postgres
  [_ config]
  (merge config {:jdbc-url (env :jdbc-database-url)}))
```
