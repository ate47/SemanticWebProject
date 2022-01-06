# SemanticWebProject

Semantic web project for my 2nd year of master

[Link to the project](https://www.emse.fr/~zimmermann/Teaching/SemWeb/Project/)

## What we need to have

- Web page to request
- Triple store (Jena?)
- ontology (knowledge model) of the triple store

## Run

### Triple store

create a database named ``database`` inside the Jena triple store

### Run backend (Dev)

```powershell
cd WebServer/
.\gradlew bootRun
```

### Run frontend (Dev)

```powershell
cd WebServer/src/frontend
npm run serve
```

### Production

To produce the production version, build and put the static version of the frontend inside Spring static as /
