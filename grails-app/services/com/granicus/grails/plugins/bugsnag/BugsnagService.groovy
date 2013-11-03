package com.granicus.grails.plugins.bugsnag

import grails.util.Environment

import com.bugsnag.Client
import com.bugsnag.MetaData

class BugsnagService {

    def grailsApplication
    def exceptionHandler
    def grailsResourceLocator

    def getConfiguredClient(context){

        log.info "getConfiguredClient()"

        // configure the client

        def conf = grailsApplication.config.grails.plugin.bugsnag

        if( !conf.enabled ){
            log.info "bugsnag plugin is not enabled. returning null."
            return null
        }

        if( !conf.containsKey('apikey') ){
            log.error "grails.plugin.bugsnag.apikey not configured. assign your bugsnag api key with this configuration value."
            return null
        }

        // create the bugsnag client
        def client = new Client( conf.apikey )

        // configure the release stage or set it to the current environment name
        client.setReleaseStage( conf.releasestage ?: Environment.current.name)

        // configure the context of the client
        if( context ){
            client.setContext( context )
        }
        else if( conf.containsKey('context') ){
            client.setContext( conf.context )
        }

      // set the application version
      client.setAppVersion( grailsApplication.metadata.getApplicationVersion() )

      return client
    }

    def notify(request, exception) {

        def client = getConfiguredClient(request.requestURI)

        // get the current user
        // get the session
        // get the request uri
        // get the forward uri
        // get the request cookies

        try{
            client.setUserId(request.remoteUser)
            MetaData metaData = new MetaData()

            metaData.addToTab( "app", "grails version", grailsApplication.metadata.getGrailsVersion() )
            metaData.addToTab( "app", "application name", grailsApplication.metadata.getApplicationName() )

            println "User principal: ${request.userPrincipal}"
            metaData.addToTab( "user", "remoteUser", request.remoteUser?:"(none)" )
            metaData.addToTab( "user", "userPrincipal", request.userPrincipal?:"(none)" )

            metaData.addToTab( "request", "requestURI", request.requestURI )
            metaData.addToTab( "request", "forwardURI", request.forwardURI)
            metaData.addToTab( "request", "cookies", request.cookies.collect{ "\nName: ${it.name}\nMax Age: ${it.maxAge}\nPath: ${it.path}\nSecure: ${it.secure}\nDomain: ${it.domain}\nVersion: ${it.version}\nValue: ${it.value}" }.join("\n") )
            metaData.addToTab( "request", "headers", request.headerNames.findAll{ it != 'cookie' }.collect{ headerName -> "${headerName}: ${request.getHeaders(headerName).toList()}" }.join('\n') )
            metaData.addToTab( "request", "authType", request.authType )
            metaData.addToTab( "request", "method", request.method )
            metaData.addToTab( "request", "server", request.serverName?:"(none)" )
            metaData.addToTab( "request", "port", request.serverPort?:"(none)" )
            metaData.addToTab( "request", "content type", request.contentType?:"(none)" )
            metaData.addToTab( "request", "character encoding", request.characterEncoding?:"(none)" )
            metaData.addToTab( "request", "scheme", request.scheme?:"(none)" )
            metaData.addToTab( "request", "queryString", request.queryString?:"(none)" )

            metaData.addToTab( "request", "xml", request.xml?.text() )
            metaData.addToTab( "request", "json", request.json?.text() )

            //TODO: get handler for including user defined metadata

            client.notify(exception,metaData)

        }catch( excp ){
            log.error "error calling notify", excp
        }
    }
}
