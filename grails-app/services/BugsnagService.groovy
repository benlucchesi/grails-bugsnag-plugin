package com.granicus.grails.plugins.bugsnag;

import com.bugsnag.Client
import com.bugsnag.MetaData

import org.springframework.beans.factory.InitializingBean
import org.codehaus.groovy.grails.commons.ConfigurationHolder as ch
import grails.util.Environment

class BugsnagService {

    def grailsApplication
    def exceptionHandler

    def getConfiguredClient(def context){

      log.info "getConfiguredClient()"
      
      // configure the client

      if( !grailsApplication.config.grails.plugin.bugsnag.enabled ){
        log.info "bugsnag plugin is not enabled. returning null."
        return null
      }

      if( !grailsApplication.config.grails.plugin.bugsnag.containsKey('apikey') ){
        log.error "grails.plugin.bugsnag.apikey not configured. assign your bugsnag api key with this configuration value."
        return null
      }

      // create the bugsnag client
      def client = new com.bugsnag.Client( grailsApplication.config.grails.plugin.bugsnag.apikey )

      // configure the release stage or set it to the current environment name
      if( grailsApplication.config.grails.plugin.bugsnag.containsKey('releasestage') ){
        client.setReleaseStage( grailsApplication.config.grails.plugin.bugsnag.releasestage )
      }
      else{
        client.setReleaseStage( Environment.current.name )
      }

      // configure the context of the client
      if( context ){
        client.setContext( context )
      }
      else if( grailsApplication.config.grails.plugin.bugsnag.containsKey('context') ){
        client.setContext( grailsApplication.config.grails.plugin.bugsnag.context )
      }

      // set the application version
      client.setAppVersion( grailsApplication.metadata.'app.version' )

      return client
    }

    def notify(def request, def response, def exception) {

      def client = getConfiguredClient(request.requestURI) 

      // get the current user
      // get the session
      // get the request uri
      // get the forward uri
      // get the response code
      // get the request cookies
    
      client.setUserId(request.remoteUser)
      MetaData metaData = new MetaData();
      metaData.addToTab( "request", "requestURI", request.requestURI );
      metaData.addToTab( "request", "forwardURI", request.forwardURI) ;
      metaData.addToTab( "request", "cookies", request.cookies.collect{ "${it.name}: ${it.value}" }.join("\n") )
      metaData.addToTab( "request", "headers", request.headerNames.collect{ "${it}: ${request.getHeaders( it )}" }.toString() )
      metaData.addToTab( "request", "authType", request.authType )
      metaData.addToTab( "request", "method", request.method )
      metaData.addToTab( "request", "queryString", request.queryString )
      
      metaData.addToTab( "response", "status", response.status )

      //TODO: get handler for including user defined metadata

      client.notify(exception,metaData)
    }
}
