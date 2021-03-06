package org.bbop.apollo.geneProduct

import grails.converters.JSON
import grails.transaction.Transactional
import org.bbop.apollo.Feature
import org.bbop.apollo.User
import org.bbop.apollo.geneProduct.GeneProduct
import org.bbop.apollo.gwt.shared.PermissionEnum
import org.bbop.apollo.history.FeatureOperation
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.restapidoc.annotation.RestApiMethod
import org.restapidoc.annotation.RestApiParam
import org.restapidoc.annotation.RestApiParams
import org.restapidoc.pojo.RestApiParamType
import org.restapidoc.pojo.RestApiVerb

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class GeneProductController {


  def permissionService
  def geneProductService
  def featureEventService
  def featureService

  @RestApiMethod(description = "Load Go Annotations for gene", path = "/geneProduct", verb = RestApiVerb.POST)
  @RestApiParams(params = [
    @RestApiParam(name = "username", type = "email", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "password", type = "password", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "uniqueName", type = "Gene uniqueName", paramType = RestApiParamType.QUERY, description = "Gene name to query on")
  ]
  )
  def index() {
    JSONObject dataObject = permissionService.handleInput(request, params)
    permissionService.checkPermissions(dataObject, PermissionEnum.READ)
    Feature feature = Feature.findByUniqueName(dataObject.uniqueName as String)
    if (feature) {
      JSONObject annotations = geneProductService.getAnnotations(feature)
      // TODO: register with marshaller
      render annotations as JSON
    } else {
      render status: NOT_FOUND
    }
  }

//        {"gene":"e35ea570-f700-41fb-b479-70aa812174ad",
//        "goTerm":"GO:0060841",
//        "geneRelationship":"RO:0002616",
//        "evidenceCode":"ECO:0000335",
//        "negate":false,
//        "withOrFrom":["withprefix:12312321"],
//        "references":["refprefix:44444444"]}
  @RestApiMethod(description = "Save New Go Annotations for gene", path = "/geneProduct/save", verb = RestApiVerb.POST)
  @RestApiParams(params = [
    @RestApiParam(name = "username", type = "email", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "password", type = "password", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "gene", type = "string", paramType = RestApiParamType.QUERY, description = "uniqueName of gene feature to query on")
    , @RestApiParam(name = "goTerm", type = "string", paramType = RestApiParamType.QUERY, description = "GO CURIE")
    , @RestApiParam(name = "goTermLabel", type = "string", paramType = RestApiParamType.QUERY, description = "GO Term Label")
    , @RestApiParam(name = "aspect", type = "string", paramType = RestApiParamType.QUERY, description = "(required) BP, MF, CC")
    , @RestApiParam(name = "geneRelationship", type = "string", paramType = RestApiParamType.QUERY, description = "Gene relationship (RO) CURIE")
    , @RestApiParam(name = "evidenceCode", type = "string", paramType = RestApiParamType.QUERY, description = "Evidence (ECO) CURIE")
    , @RestApiParam(name = "evidenceCodeLAbel", type = "string", paramType = RestApiParamType.QUERY, description = "Evidence (ECO) Label")
    , @RestApiParam(name = "negate", type = "boolean", paramType = RestApiParamType.QUERY, description = "Negate evidence (default false)")
    , @RestApiParam(name = "withOrFrom", type = "string", paramType = RestApiParamType.QUERY, description = "JSON Array of with or from CURIE strings, e.g., {[\"UniProtKB:12312]]\"]}")
    , @RestApiParam(name = "references", type = "string", paramType = RestApiParamType.QUERY, description = "JSON Array of reference CURIE strings, e.g., {[\"PMID:12312]]\"]}")
  ]
  )
  @Transactional
  def save() {
    JSONObject dataObject = permissionService.handleInput(request, params)
    permissionService.checkPermissions(dataObject, PermissionEnum.WRITE)
    User user = permissionService.getCurrentUser(dataObject)
    GeneProduct geneProduct = new GeneProduct()
    Feature feature = Feature.findByUniqueName(dataObject.gene)

    JSONObject originalFeatureJsonObject = featureService.convertFeatureToJSON(feature)

    geneProduct.feature = feature
    geneProduct.productName = dataObject.productName
    geneProduct.evidenceRef = dataObject.evidenceCode
    geneProduct.evidenceRefLabel = dataObject.evidenceCodeLabel
    geneProduct.alternate = dataObject.alternate ?: false
    geneProduct.withOrFromArray = dataObject.withOrFrom
    geneProduct.notesArray = dataObject.notes
    geneProduct.reference = dataObject.reference
    geneProduct.lastUpdated = new Date()
    geneProduct.dateCreated = new Date()
    geneProduct.addToOwners(user)
    feature.addToGeneProducts(geneProduct)
    geneProduct.save(flush: true, failOnError: true)

    JSONArray oldFeaturesJsonArray = new JSONArray()
    oldFeaturesJsonArray.add(originalFeatureJsonObject)
    JSONArray newFeaturesJsonArray = new JSONArray()
    JSONObject currentFeatureJsonObject = featureService.convertFeatureToJSON(feature)
    newFeaturesJsonArray.add(currentFeatureJsonObject)

    featureEventService.addNewFeatureEvent(FeatureOperation.ADD_GO_ANNOTATION,
      feature.name,
      feature.uniqueName,
      dataObject,
      oldFeaturesJsonArray,
      newFeaturesJsonArray,
      user)

    JSONObject annotations = geneProductService.getAnnotations(feature)
    render annotations as JSON
  }

  @RestApiMethod(description = "Update existing Go Annotations for gene", path = "/geneProduct/update", verb = RestApiVerb.POST)
  @RestApiParams(params = [
    @RestApiParam(name = "username", type = "email", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "password", type = "password", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "id", type = "string", paramType = RestApiParamType.QUERY, description = "GO Annotation ID to update (required)")
    , @RestApiParam(name = "gene", type = "string", paramType = RestApiParamType.QUERY, description = "uniqueName of gene feature to query on")
    , @RestApiParam(name = "goTerm", type = "string", paramType = RestApiParamType.QUERY, description = "GO CURIE")
    , @RestApiParam(name = "goTermLabel", type = "string", paramType = RestApiParamType.QUERY, description = "GO Term Label")
    , @RestApiParam(name = "aspect", type = "string", paramType = RestApiParamType.QUERY, description = "(required) BP, MF, CC")
    , @RestApiParam(name = "geneRelationship", type = "string", paramType = RestApiParamType.QUERY, description = "Gene relationship (RO) CURIE")
    , @RestApiParam(name = "evidenceCode", type = "string", paramType = RestApiParamType.QUERY, description = "Evidence (ECO) CURIE")
    , @RestApiParam(name = "evidenceCodeLabel", type = "string", paramType = RestApiParamType.QUERY, description = "Evidence (ECO) Label")
    , @RestApiParam(name = "negate", type = "boolean", paramType = RestApiParamType.QUERY, description = "Negate evidence (default false)")
    , @RestApiParam(name = "withOrFrom", type = "string", paramType = RestApiParamType.QUERY, description = "JSON Array of with or from CURIE strings, e.g., {[\"UniProtKB:12312]]\"]}")
    , @RestApiParam(name = "references", type = "string", paramType = RestApiParamType.QUERY, description = "JSON Array of reference CURIE strings, e.g., {[\"PMID:12312]]\"]}")
  ]
  )
  @Transactional
  def update() {
    JSONObject dataObject = permissionService.handleInput(request, params)
    permissionService.checkPermissions(dataObject, PermissionEnum.WRITE)
    User user = permissionService.getCurrentUser(dataObject)
    Feature feature = Feature.findByUniqueName(dataObject.gene)

    JSONObject originalFeatureJsonObject = featureService.convertFeatureToJSON(feature)


    GeneProduct geneProduct = GeneProduct.findById(dataObject.id)
    geneProduct.feature = feature
    geneProduct.productName = dataObject.productName
    geneProduct.evidenceRef = dataObject.evidenceCode
    geneProduct.evidenceRefLabel = dataObject.evidenceCodeLabel
    geneProduct.alternate = dataObject.alternate ?: false
    geneProduct.withOrFromArray = dataObject.withOrFrom
    geneProduct.notesArray = dataObject.notes
    geneProduct.reference = dataObject.reference
    geneProduct.lastUpdated = new Date()
    geneProduct.dateCreated = new Date()
    geneProduct.addToOwners(user)
    geneProduct.save(flush: true, failOnError: true, insert: false)

    JSONArray oldFeaturesJsonArray = new JSONArray()
    oldFeaturesJsonArray.add(originalFeatureJsonObject)
    JSONArray newFeaturesJsonArray = new JSONArray()
    JSONObject currentFeatureJsonObject = featureService.convertFeatureToJSON(feature)
    newFeaturesJsonArray.add(currentFeatureJsonObject)

    featureEventService.addNewFeatureEvent(FeatureOperation.UPDATE_GO_ANNOTATION,
      feature.name,
      feature.uniqueName,
      dataObject,
      oldFeaturesJsonArray,
      newFeaturesJsonArray,
      user)

    JSONObject annotations = geneProductService.getAnnotations(feature)
    render annotations as JSON
  }

  @RestApiMethod(description = "Delete existing Go Annotations for gene", path = "/geneProduct/delete", verb = RestApiVerb.POST)
  @RestApiParams(params = [
    @RestApiParam(name = "username", type = "email", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "password", type = "password", paramType = RestApiParamType.QUERY)
    , @RestApiParam(name = "id", type = "string", paramType = RestApiParamType.QUERY, description = "GO Annotation ID to delete (required)")
    , @RestApiParam(name = "uniqueName", type = "string", paramType = RestApiParamType.QUERY, description = "Gene uniqueName to remove feature from")
  ]
  )
  @Transactional
  def delete() {
    JSONObject dataObject = permissionService.handleInput(request, params)
    permissionService.checkPermissions(dataObject, PermissionEnum.WRITE)
    User user = permissionService.getCurrentUser(dataObject)

    Feature feature = Feature.findByUniqueName(dataObject.gene)
    JSONObject originalFeatureJsonObject = featureService.convertFeatureToJSON(feature)

    GeneProduct geneProduct = GeneProduct.findById(dataObject.id)
    feature.removeFromGeneProducts(geneProduct)
    geneProduct.delete(flush: true)

    JSONArray oldFeaturesJsonArray = new JSONArray()
    oldFeaturesJsonArray.add(originalFeatureJsonObject)
    JSONArray newFeaturesJsonArray = new JSONArray()
    JSONObject currentFeatureJsonObject = featureService.convertFeatureToJSON(feature)
    newFeaturesJsonArray.add(currentFeatureJsonObject)

    featureEventService.addNewFeatureEvent(FeatureOperation.REMOVE_GO_ANNOTATION,
      feature.name,
      feature.uniqueName,
      dataObject,
      oldFeaturesJsonArray,
      newFeaturesJsonArray,
      user)

    JSONObject annotations = geneProductService.getAnnotations(feature)
    render annotations as JSON
  }
}
