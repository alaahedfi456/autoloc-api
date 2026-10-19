# Atelier 4 : Couche Service, Inversion de Contrôle (IoC) et Injection de Dépendances (DI)

## A. Modes d'injection comparés

Ce tableau synthétise la comparaison entre l'injection par constructeur (bonne pratique AutoLoc) et l'injection par attribut (`@Autowired`) telle qu'analysée lors de l'Étape 2 :

| Critère d'évaluation | Injection par constructeur | Injection par attribut (`@Autowired`) |
| :--- | :---: | :---: |
| **Champ `final` possible ?** | **Oui** (garantit l'immuabilité et la complétude de l'objet) | **Non** (initialisation tardive par réflexion) |
| **Dépendance visible dans la signature ?** | **Oui** (explicite dans le constructeur de la classe) | **Non** (dépendances invisibles de l'extérieur) |
| **Utilisation avec `new` hors conteneur sans dépendance `null` ?** | **Oui** (permet de passer des mocks en tests unitaires) | **Non** (champs laissés à `null`, risque de `NullPointerException`) |
| **Signalé par SonarQube for IDE ?** | **Non** (conforme aux règles de qualité) | **Oui** (règle **S6813** : *"Field injection is not recommended"*) |

---

## B. Services

Tableau récapitulatif des neuf services de l'application `autoloc-api`, avec leurs dépendances injectées et justifications architecturales :

| Service | Dépendances injectées | Mode d'injection | Justification |
| :--- | :--- | :--- | :--- |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `ContratServiceImpl` | `IContratRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire pour les opérations en lecture seule, champ `final` immuable, abstraction via interface. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur (`@RequiredArgsConstructor`) | Dépendance obligatoire, champ `final` immuable, abstraction via interface, testable unitairement sans conteneur. |

---

## C. Messages d'erreur du conteneur Spring

Analyse et résolution des trois messages d'erreur types rencontrés lors de l'assemblage des beans (Étape 3) :

### Message 1 : Bean introuvable
```text
APPLICATION FAILED TO START
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.ContratController
required a bean of type 'tn.esprit.autoloc.service.IContratService' that could not be found.
Action:
Consider defining a bean of type 'tn.esprit.autoloc.service.IContratService' in your configuration.
```
* **Exception sous-jacente** : `NoSuchBeanDefinitionException` (encapsulée dans `UnsatisfiedDependencyException`).
* **Bean concerné** : Le bean implémentant `IContratService` (`ContratServiceImpl`).
* **Deux causes les plus probables côté `ContratServiceImpl`** :
  1. L'annotation de stéréotype `@Service` (ou `@Component`) a été omise sur la classe `ContratServiceImpl`.
  2. La classe `ContratServiceImpl` se situe dans un package externe au package racine scanné par `@SpringBootApplication` (`tn.esprit.autoloc`).
* **Correction** : Ajouter l'annotation `@Service` sur `ContratServiceImpl` et veiller à ce que la classe soit positionnée dans `tn.esprit.autoloc.service`.

### Message 2 : Ambiguïté de beans (plusieurs candidats)
```text
Description:
Parameter 0 of constructor in tn.esprit.autoloc.web.controller.NotificationController
required a single bean, but 2 were found:
 - emailNotificateur
 - smsNotificateur
Action:
Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```
* **Exception sous-jacente** : `NoUniqueBeanDefinitionException` (encapsulée dans `UnsatisfiedDependencyException`).
* **Bean concerné** : Le point d'injection du contrôleur `NotificationController`.
* **Deux corrections possibles et emplacement des annotations** :
  1. **Option `@Primary`** : Placer l'annotation `@Primary` sur la classe de l'implémentation privilégiée par défaut (ex. `@Primary @Component public class EmailNotificateur implements INotificateur`).
  2. **Option `@Qualifier`** : Placer l'annotation `@Qualifier("emailNotificateur")` au niveau du consommateur, sur le paramètre du constructeur recevant la dépendance (ex. `public NotificationController(@Qualifier("emailNotificateur") INotificateur notificateur)`).

### Message 3 : Dépendance circulaire
```text
The dependencies of some of the beans in the application context form a cycle:
 clientServiceImpl
 | ^
 v |
 reservationServiceImpl
```
* **Exception sous-jacente** : `BeanCurrentlyInCreationException`.
* **Pourquoi l'injection par constructeur rend-elle ce cycle impossible à contourner ?**
  Avec l'injection par constructeur, Spring doit instancier tous les arguments du constructeur avant de pouvoir créer l'objet lui-même. Si le constructeur de `ClientServiceImpl` attend `ReservationServiceImpl`, et que le constructeur de `ReservationServiceImpl` attend `ClientServiceImpl`, Spring entre dans une impasse : aucun des deux beans ne peut être instancié en premier.
* **Piste de conception pour supprimer ce cycle** :
  Extraire la logique métier partagée dans un troisième service intermédiaire (ex. `ReservationValidationService` ou `ClientReservationCoordinator`), ou utiliser le mécanisme des événements Spring (`ApplicationEventPublisher`) pour découpler temporellement et structurellement les deux services.

---

## D. Anomalies SonarQube for IDE

Ce tableau documente les trois anomalies identifiées et analysées dans le cadre des étapes guidées de l'Atelier 4 (Étape 2 pour l'injection par champ, Étape 4 pour l'exception générique, et respect des conventions Clean Code / JPA) :

| Règle / Problématique | Explication du problème | Correction / Prévention appliquée |
| :--- | :--- | :--- |
| **S6813** : *Field dependency injection should be avoided* (Étape 2) | Règle déclenchée lors de l'expérimentation de l'injection par attribut `@Autowired`. Elle masque les dépendances réelles de la classe, empêche l'immuabilité (champ non `final`) et rend l'objet incomplet hors conteneur Spring (tests unitaires avec `new`). | Remplacement par l'injection par constructeur via l'annotation Lombok `@RequiredArgsConstructor` sur des champs `private final` de type interface. |
| **S112** : *Generic exceptions should never be thrown* (Étape 4) | Règle déclenchée lors de l'expérimentation consistant à remplacer temporairement l'exception métier par une `RuntimeException` générique. Elle empêche un traitement ciblé des erreurs par la couche appelante. | Création et utilisation systématique de l'exception métier dédiée `ResourceNotFoundException` étendant `RuntimeException`. |
| **Clean Code & Conventions JPA** (Prérequis & Étape 4) | L'utilisation de `@Data` sur les entités JPA génère automatiquement des méthodes `equals()`, `hashCode()` et `toString()` provoquant des boucles infinies (`StackOverflowError`) sur les relations bidirectionnelles. De même, les imports inutilisés polluent le code. | Maintien d'annotations Lombok ciblées (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) sans `@Data`, et nettoyage strict des imports dans l'ensemble des classes et interfaces. |

---

## E. Questions de compréhension

### 1. Où est le `new` de `ContratServiceImpl` et qui l'exécute ?
Il n'y a aucun appel explicite à `new ContratServiceImpl(...)` dans le code source écrit par le développeur. C'est le conteneur Spring IoC (`ApplicationContext` / `BeanFactory`) qui instancie la classe par introspection (réflexion Java) au démarrage de l'application (`SpringApplication.run`), lors du composant-scan déclenché par `@SpringBootApplication`.

### 2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?
Pour respecter le principe d'Inversion des Dépendances (D de SOLID) et garantir un couplage faible entre les couches. Le contrôleur s'appuie sur une abstraction (le contrat d'interface). Cela permet d'isoler le contrôleur des détails d'implémentation, de substituer facilement le service par un faux objet (mock) lors des tests unitaires, ou d'interchanger l'implémentation sans toucher au contrôleur.

### 3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.
Les beans de service Spring ont une portée (`scope`) singleton par défaut : une seule et unique instance est partagée par l'ensemble des threads de l'application (toutes les requêtes HTTP concurrentes). Si un service stockait un état mutable propre à une requête (ex. un champ d'instance `private Contrat contratEnCours;`), des requêtes exécutées en parallèle par différents utilisateurs écraseraient mutuellement cette valeur. Par exemple, l'utilisateur A valide son contrat, mais pendant le traitement, la requête de l'utilisateur B écrase `contratEnCours` : l'utilisateur A se verrait alors enregistrer ou modifier le contrat de l'utilisateur B (race condition).

### 4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?
Appeler directement `save(contrat)` avec l'objet reçu déclencherait un `merge` Hibernate qui écraserait toutes les colonnes de la base de données. Les champs non fournis dans le corps de la requête (valant `null`) écraseraient les données valides existantes. De plus, sur des associations avec `orphanRemoval = true` (comme `paiements`), passer une collection vide ou nulle supprimerait automatiquement en cascade tous les paiements associés. Le rechargement préalable (`findById`) permet d'assurer la présence de l'entité, de ne modifier que les champs autorisés, et de préserver l'identifiant ainsi que les collections existantes.

### 5. Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?
* **`@Component` vs `@Bean`** : `@Component` s'applique sur une classe de notre projet pour la désigner comme bean géré par le scan automatique de Spring. `@Bean` s'applique sur une méthode au sein d'une classe `@Configuration` pour construire et enregistrer manuellement une instance de classe (indispensable pour les classes issues de bibliothèques tierces ou du JDK qu'on ne peut pas annoter directement).
* **`@Primary` vs `@Qualifier`** : `@Primary` se place sur une classe candidate pour lui conférer la priorité par défaut en cas d'ambiguïté de type. `@Qualifier("nom")` se place à l'injection pour désigner explicitement le bean ciblé par son identifiant.
* **`IPaiementService` en lecture seule** : Dans le modèle du domaine AutoLoc, un paiement ne vit pas de manière autonome : il est consécutif et subordonné à un contrat. Ses opérations de création, modification ou suppression s'effectuent exclusivement via l'agrégat parent `Contrat` (fil rouge `Contrat ↔ Paiement`).
