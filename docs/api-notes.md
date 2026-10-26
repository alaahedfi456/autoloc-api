# Atelier 5 : Spring MVC et SpringDoc — Exposer, Tester et Documenter l'API REST AutoLoc

Ce document synthétise les travaux de conception, de développement, de test et de documentation réalisés pour la couche Web (API REST) du projet `autoloc-api`.

---

## A. Endpoints de l'API REST

Tableau récapitulatif des routes exposées par les 9 contrôleurs du package `tn.esprit.autoloc.web.controller`. Grâce à `server.servlet.context-path=/api`, toutes les URL complètes sont préfixées par `/api`.

| Contrôleur | Verbe & Route | Service appelé | Code succès | Codes d'erreur | Description fonctionnelle |
| :--- | :--- | :--- | :---: | :---: | :--- |
| `ContratController` | `GET /api/contrats` | `contratService.findAll()` | 200 | — | Liste de tous les contrats de location. |
| `ContratController` | `GET /api/contrats/{id}` | `contratService.findById(id)` | 200 | 404 | Consultation détaillée d'un contrat par son identifiant. |
| `ContratController` | `POST /api/contrats` | `contratService.create(contrat)` | 201 | 400 | Création d'un contrat (avec ou sans paiements imbriqués). |
| `ContratController` | `PUT /api/contrats/{id}` | `contratService.update(id, contrat)` | 200 | 400, 404 | Modification des informations modifiables d'un contrat. |
| `ContratController` | `DELETE /api/contrats/{id}` | `contratService.deleteById(id)` | 204 | 404 | Suppression d'un contrat et suppression en cascade de ses paiements liés. |
| `PaiementController` | `GET /api/paiements` | `paiementService.findAll()` | 200 | — | Liste de tous les paiements enregistrés. |
| `PaiementController` | `GET /api/paiements/{id}` | `paiementService.findById(id)` | 200 | 404 | Consultation d'un paiement spécifique. |
| `AgenceController` | `GET /api/agences` | `agenceService.findAll()` | 200 | — | Liste des agences de location. |
| `AgenceController` | `GET /api/agences/{id}` | `agenceService.findById(id)` | 200 | 404 | Consultation d'une agence. |
| `AgenceController` | `POST /api/agences` | `agenceService.create(agence)` | 201 | 400 | Création d'une agence (nom obligatoire). |
| `AgenceController` | `PUT /api/agences/{id}` | `agenceService.update(id, agence)` | 200 | 400, 404 | Mise à jour des coordonnées d'une agence. |
| `AgenceController` | `DELETE /api/agences/{id}` | `agenceService.deleteById(id)` | 204 | 404 | Suppression d'une agence. |
| `ClientController` | `GET /api/clients` | `clientService.findAll()` | 200 | — | Liste des clients inscrits. |
| `ClientController` | `GET /api/clients/{id}` | `clientService.findById(id)` | 200 | 404 | Consultation du profil d'un client. |
| `ClientController` | `POST /api/clients` | `clientService.create(client)` | 201 | 400 | Enregistrement d'un nouveau client (nom obligatoire). |
| `ClientController` | `PUT /api/clients/{id}` | `clientService.update(id, client)` | 200 | 400, 404 | Mise à jour du profil d'un client. |
| `ClientController` | `DELETE /api/clients/{id}` | `clientService.deleteById(id)` | 204 | 404 | Suppression d'un compte client. |
| `EmployeController` | `GET /api/employes` | `employeService.findAll()` | 200 | — | Liste des employés de l'entreprise. |
| `EmployeController` | `GET /api/employes/{id}` | `employeService.findById(id)` | 200 | 404 | Consultation des informations d'un employé. |
| `EmployeController` | `POST /api/employes` | `employeService.create(employe)` | 201 | 400 | Création d'un employé rattaché à une agence. |
| `EmployeController` | `PUT /api/employes/{id}` | `employeService.update(id, employe)` | 200 | 400, 404 | Modification d'un employé. |
| `EmployeController` | `DELETE /api/employes/{id}` | `employeService.deleteById(id)` | 204 | 404 | Suppression d'un employé. |
| `VehiculeController` | `GET /api/vehicules` | `vehiculeService.findAll()` | 200 | — | Liste du parc de véhicules. |
| `VehiculeController` | `GET /api/vehicules/{id}` | `vehiculeService.findById(id)` | 200 | 404 | Consultation d'un véhicule par son identifiant. |
| `VehiculeController` | `POST /api/vehicules` | `vehiculeService.create(vehicule)` | 201 | 400 | Ajout d'un véhicule (tarif journalier non négatif). |
| `VehiculeController` | `PUT /api/vehicules/{id}` | `vehiculeService.update(id, vehicule)` | 200 | 400, 404 | Mise à jour d'un véhicule. |
| `VehiculeController` | `DELETE /api/vehicules/{id}` | `vehiculeService.deleteById(id)` | 204 | 404 | Suppression d'un véhicule. |
| `EquipementController` | `GET /api/equipements` | `equipementService.findAll()` | 200 | — | Liste des équipements optionnels. |
| `EquipementController` | `GET /api/equipements/{id}` | `equipementService.findById(id)` | 200 | 404 | Consultation d'un équipement. |
| `EquipementController` | `POST /api/equipements` | `equipementService.create(equipement)` | 201 | 400 | Création d'un équipement (libellé obligatoire). |
| `EquipementController` | `PUT /api/equipements/{id}` | `equipementService.update(id, equipement)` | 200 | 400, 404 | Modification d'un équipement. |
| `EquipementController` | `DELETE /api/equipements/{id}` | `equipementService.deleteById(id)` | 204 | 404 | Suppression d'un équipement. |
| `ReservationController` | `GET /api/reservations` | `reservationService.findAll()` | 200 | — | Liste des réservations de véhicules. |
| `ReservationController` | `GET /api/reservations/{id}` | `reservationService.findById(id)` | 200 | 404 | Consultation d'une réservation. |
| `ReservationController` | `POST /api/reservations` | `reservationService.create(reservation)` | 201 | 400 | Création d'une réservation (dates cohérentes). |
| `ReservationController` | `PUT /api/reservations/{id}` | `reservationService.update(id, reservation)` | 200 | 400, 404 | Modification d'une réservation. |
| `ReservationController` | `DELETE /api/reservations/{id}` | `reservationService.deleteById(id)` | 204 | 404 | Annulation / suppression d'une réservation. |
| `MaintenanceController` | `GET /api/maintenances` | `maintenanceService.findAll()` | 200 | — | Liste des opérations d'entretien. |
| `MaintenanceController` | `GET /api/maintenances/{id}` | `maintenanceService.findById(id)` | 200 | 404 | Consultation d'une fiche de maintenance. |
| `MaintenanceController` | `POST /api/maintenances` | `maintenanceService.create(maintenance)` | 201 | 400 | Enregistrement d'une maintenance (dates valides). |
| `MaintenanceController` | `PUT /api/maintenances/{id}` | `maintenanceService.update(id, maintenance)` | 200 | 400, 404 | Mise à jour d'une maintenance. |
| `MaintenanceController` | `DELETE /api/maintenances/{id}` | `maintenanceService.deleteById(id)` | 204 | 404 | Suppression d'une fiche de maintenance. |

---

## B. Annotations Jackson et Sérialisation Bidirectionnelle

### 1. Associations annotées et justifications

| Association | Entité source & champ | Annotation retenue | Justification |
| :--- | :--- | :--- | :--- |
| **Contrat $\leftrightarrow$ Paiement** (Composition) | `Contrat.paiements` | `@JsonManagedReference("contrat-paiements")` | Partie directe : sérialisée normalement dans le JSON du parent. Permet à Jackson de rattacher l'instance de `Contrat` aux `Paiement` enfants lors de la désérialisation d'un `POST`. |
| **Contrat $\leftrightarrow$ Paiement** (Composition) | `Paiement.contrat` | `@JsonBackReference("contrat-paiements")` | Partie inverse : omise lors de la sérialisation JSON pour briser le cycle infini (`StackOverflowError`), tout en restaurant automatiquement le lien bidirectionnel en mémoire. |
| **Contrat $\leftrightarrow$ Réservation** | `Contrat.reservation` | `@JsonIgnore` | Évite la boucle de sérialisation entre le contrat et sa réservation sans masquer la relation côté réservation. |
| **Agence $\leftrightarrow$ Employé** | `Agence.employes` | `@JsonIgnore` | L'employé désigne son agence par son identifiant (`{"agence": {"idAgence": 1}}`). La liste inverse côté agence est ignorée pour éviter d'embarquer récursivement tout le personnel. |
| **Agence $\leftrightarrow$ Véhicule** | `Agence.vehicules` | `@JsonIgnore` | Même raison : le véhicule référence son agence, mais l'agence n'embarque pas la liste de ses véhicules dans sa propre réponse JSON. |
| **Véhicule $\leftrightarrow$ Réservation** | `Vehicule.reservations` | `@JsonIgnore` | Empêche le cycle `Vehicule -> reservations -> vehicule`. |
| **Client $\leftrightarrow$ Réservation** | `Client.reservations` | `@JsonIgnore` | Conforme à l'avertissement de l'atelier : ne pas utiliser `@JsonBackReference` sur `Reservation.client` car la propriété serait ignorée en désérialisation, créant une réservation orpheline. L'ignorance est donc placée côté `Client`. |
| **Véhicule $\leftrightarrow$ Équipement** | `Equipement.vehicules` | `@JsonIgnore` | Rompt le cycle `ManyToMany` entre véhicules et équipements. |

### 2. Différence fondamentale entre `@JsonBackReference` et `@JsonIgnore`

* **`@JsonBackReference`** :
  * Conçue spécifiquement pour fonctionner en binôme avec `@JsonManagedReference` sur une relation parent-enfant (composition).
  * Lors de la **sérialisation** (Java $\rightarrow$ JSON), le champ est ignoré pour éviter la boucle.
  * Lors de la **désérialisation** (JSON $\rightarrow$ Java), Jackson réinjecte automatiquement l'objet parent (`Contrat`) dans chaque enfant (`Paiement`), ce qui garantit l'intégrité de la relation bidirectionnelle avant la persistance JPA en cascade.
* **`@JsonIgnore`** :
  * Annotation générale qui masque totalement le champ, tant à la **sérialisation** qu'à la **désérialisation**.
  * Si elle était utilisée sur `Paiement.contrat`, le champ `contrat` resterait `null` lors de la réception d'un JSON parent-enfant, rendant impossible la persistance en cascade de la clé étrangère `id_contrat`.

---

## C. Rôles comparés de Postman et Swagger / OpenAPI

Postman et Swagger UI sont deux outils complémentaires indispensables dans le cycle de vie d'une API REST :

| Outil | Rôle principal | Complémentarité & Apport spécifique | Exemple concret dans l'Atelier 5 |
| :--- | :--- | :--- | :--- |
| **Postman** | **Client de test dynamique et automatisable** | Permet d'envoyer des requêtes HTTP réelles, d'orchestrer des scénarios séquentiels avec conservation d'état (variables d'environnement), et d'exécuter des scripts d'assertions Javascript (`pm.test`). | **Validation du scénario cascade** : `POST /api/contrats` avec 2 paiements $\rightarrow$ extraction automatique de `contratId` et `paiementId` $\rightarrow$ vérification de la présence des paiements dans `GET /api/paiements` $\rightarrow$ `DELETE /api/contrats/{{contratId}}` $\rightarrow$ vérification que `GET /api/paiements/{{paiementId}}` renvoie bien 404 (cascade REMOVE). |
| **Swagger UI / SpringDoc** | **Documentation vivante synchronisée avec le code** | Fournit une spécification OpenAPI standardisée générée automatiquement par introspection du code Java, servant de contrat d'interface lisible pour les équipes front-end et de banc d'essai interactif sans installation cliente. | **Exploration du modèle et des codes retour** : La page `/api/swagger-ui.html` expose immédiatement les schémas des entités, les statuts possibles (200, 201, 204, 400, 404) et permet à un développeur tiers de tester `GET /api/contrats` ou d'inspecter les paramètres sans aucun fichier de collection externe. |

---

## D. Analyse de la Qualité du Code (SonarQube for IDE)

Deux anomalies d'architecture et de qualité typiques ont été relevées et traitées :

### Anomalie 1 : Injection de dépendance par constructeur vs par attribut (`@Autowired`)
* **Règle SonarQube** : `java:S6813` (*"Field injection is not recommended"*).
* **Explication** : L'injection par champ (`@Autowired private IContratService contratService;`) viole l'encapsulation, empêche de déclarer les dépendances `final` (immuabilité impossible) et interdit l'instanciation manuelle en tests unitaires sans framework de réflexion.
* **Correction appliquée** : Utilisation systématique de champs `private final` et de l'annotation Lombok `@RequiredArgsConstructor` sur tous les contrôleurs :
  ```java
  @RestController
  @RequestMapping("/contrats")
  @RequiredArgsConstructor
  public class ContratController {
      private final IContratService contratService;
      ...
  }
  ```

### Anomalie 2 : Absence de centralisation de la gestion des exceptions (500 inattendu)
* **Règle SonarQube** : `java:S1166` (*"Exception handlers should preserve the original exceptions"*) et `java:S112` (*"Generic exceptions should never be thrown"*).
* **Explication** : Laisser remonter `ResourceNotFoundException` ou `IllegalArgumentException` jusqu'au conteneur servlet produisait une erreur serveur non gérée HTTP 500, masquant l'erreur métier et renvoyant une trace de pile non sécurisée au client.
* **Correction appliquée** : Création d'un composant dédié `@RestControllerAdvice` (`GlobalExceptionHandler`) traduisant proprement les exceptions en codes HTTP normalisés (404, 400, 405) avec un payload JSON d'erreur propre et horodaté.

---

## E. Réponses aux Questions de Compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?
* **Composant récepteur** : La servlet unique de façade de Spring MVC, la **`DispatcherServlet`** (*Front Controller*).
* **Mécanisme de résolution** : Elle consulte le composant **`HandlerMapping`** (notamment `RequestMappingHandlerMapping`), qui maintient une table de correspondance en mémoire construite au démarrage de l'application en inspectant les annotations `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc., l'URL de la requête, le verbe HTTP et les en-têtes `Content-Type`/`Accept`.

### 2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?
* **Non-dépendance envers `ContratRepository`** : Respect du découpage en couches. Le contrôleur web ne doit jamais contenir ni orchestrer de persistance ou de logique métier directe ; c'est le rôle exclusif de la couche Service.
* **Dépendance envers l'interface `IContratService` plutôt que la classe concrète `ContratServiceImpl`** : Principe d'inversion des dépendances (DIP des principes SOLID). Cela permet un couplage faible, autorise le remplacement ou la décoration de l'implémentation sans modifier le contrôleur, et facilite le test unitaire en permettant d'injecter des mocks de l'interface sans démarrer Spring.

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?
* **POST $\rightarrow$ 201 (Created)** : La norme HTTP stipule que la création réussie d'une ressource sur le serveur doit être notifiée par le statut 201 avec généralement l'URI ou la représentation de la ressource créée.
* **DELETE $\rightarrow$ 204 (No Content)** : La ressource a été supprimée avec succès et le serveur n'a plus aucun corps de données à renvoyer au client.
* **Ressource absente $\rightarrow$ 404 et non 500** :
  * Le code **404 (Not Found)** est une erreur **côté client** (4xx) indiquant que l'URL/identifiant demandé n'existe pas dans le système.
  * Le code **500 (Internal Server Error)** est une erreur **côté serveur** indiquant un bogue ou une défaillance interne imprévue du serveur. Une ressource inexistante n'étant pas une panne du serveur mais un cas normal d'absence de données, elle doit impérativement être signalée par un 404.

### 4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?
* **Uniquement des GET** : Dans le modèle métier d'AutoLoc, le cycle de vie d'un paiement est indissociable de son contrat de location (composition forte). Un paiement ne peut exister seul sans contrat : sa création et sa suppression sont gérées en cascade par le contrat lui-même (`Contrat -> paiements`). `PaiementService` et `PaiementController` sont donc volontairement restreints à la consultation en lecture seule.
* **Réponse à un POST `/api/paiements`** : Spring MVC ne trouvant aucune méthode mappée sur le verbe POST pour ce chemin renvoie une exception `HttpRequestMethodNotSupportedException`, traduite par notre `GlobalExceptionHandler` en **HTTP 405 Method Not Allowed** avec un corps JSON d'erreur.

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?
* **Risques de l'exposition directe des entités JPA** :
  1. **Couplage fort** : L'API publique est calquée sur le schéma physique de la base de données. Toute modification de colonne casse les clients de l'API.
  2. **Fuite de données internes sensibles** : Exposition de mots de passe, clés techniques ou informations d'audit internes.
  3. **Problèmes de sérialisation et performances** : Risque de récursion infinie (boucles cycliques) et déclenchement intempestif de requêtes SQL tardives (*Lazy Loading N+1* / erreurs de session `ByteBuddyInterceptor`).
* **`Contrat.paiements` (`@JsonManagedReference`) vs `Agence.vehicules` (`@JsonIgnore`)** :
  * `Contrat ↔ Paiement` est une **composition** : le contrat doit embarquer ses paiements lors de la création d'un contrat complet via le JSON parent (cascade PERSIST). `@JsonManagedReference` sérialise les paiements et permet à Jackson de reconstituer automatiquement le lien bidirectionnel enfant $\rightarrow$ parent sans boucle.
  * `Agence ↔ Véhicule` est une **agrégation simple** : les véhicules ont leur propre cycle de vie indépendant. Un véhicule est créé séparément en désignant son agence par son identifiant (`{"agence": {"idAgence": 1}}`). Inclure la liste exhaustive de tous les véhicules dans chaque retour d'agence alourdirait inutilement la réponse et créerait un cycle de sérialisation, d'où l'emploi de `@JsonIgnore`.
