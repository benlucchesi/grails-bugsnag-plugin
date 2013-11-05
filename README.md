# Bugsnag Grails Plugin

Current Version: 0.1

The bugsnag grails plugin integrates the bugsnag client into a grails application and automatically reports errors to the bugsnag service (www.bugsnag.com). Information reported includes a dump of the original request (headers, cookies, query string, original URL, grails environment, etc), the currently logged in user, and a stacktrace of the exception.

## Installation

compile ":bugsnag:0.2"

## Version History
  <ul>
    <li>0.2 - added metadata customization, added session dump to report</li>
    <li>0.1 - first release</li>
  </ul>

## Usage

Simply enabling the bugsnag plugin automatically configures error reporting capabilities. All uncaught exceptions will be reported to bugsnag.

### Example 
Config.groovy
    grails.plugin.bugsnag.enabled = true
    grails.plugin.bugsnag.apikey = "<bugsnag API key>"

### Configuration Parameters

<table>
  <thead>
    <tr>
      <th>name</th>
      <th>default</th>
      <th>description</th>
  </thead>
  <tbody>
    <tr>
      <td>grails.plugin.bugsnag.enabled</td>
      <td>false</td>
      <td>enable or disables bugsnag.</td>
    </tr>
    <tr>
      <td>grails.plugin.bugsnag.apikey</td>
      <td>(not set)</td>
      <td>API Key provided by bugsnag to report to.</td>
    </tr>
  </tbody>
</table>

In addition to automatic error reporting, the bugsnag plugin implements bugsnagService which can be used directly in your application to send notifications. The following is an example of how to use bugsnagService to report caught exceptions.
     
      // in a controller class
      def bugsnagService  // autowired

      // somewhere in a method
      def index() {
        try{
          // something breaks
        }
        catch( excp ){
          // handle exception

          def mapOfExtraMetaData = [:] // 
          bugsnagService.notify(request,excp,mapOfExtraMetadData) // assumes you're calling from a controller where the request object is in scope
        }

        //... render something eventually
      }

### bugsnagService API

<table>
  <thead>
    <th>method name</th>
    <th>description</th>
    <th>parameters</th>
    <th>returns</th>
  </thead>
  <tbody>
    <tr>
      <td>
getConfiguredClient
      </td>
      <td>
Gets a fully configured Client object.
      </td>
      <td>
<ul>
<li>context - string used to describe where the exception occurred.</li>
</ul>
      </td>
      <td>
com.bugsnag.Client object
      </td>
    </tr>
    <tr>
      <td>
notify
      </td>
      <td>
sends a configured notification to bugsnag.
      </td>
      <td>
        <ul>
          <li>
            request - HttpServletRequest
          </li>
          <li>
            exception - java.lang.Exception
          </li>
          <li>
            extraData - (optional) map of data to include in exception report
          </li>
        </ul>
      </td>
      <td>
nothing
      </td>   
    </tr>
  </tbody>
</table>

### version 0.2+
The bugsnagService has the ability to add user-defined metaData to reports. To use this feature, add the bugsnagService to BootStrap.groovy and assign a closure to the addMetadata property. In the closure, add code that assigns values to the provided metaData class instance. The addMetadata closure will be called everytime is notification is sent bugsnag.

#### example BootStrap.groovy
    def bugsnagService

    def init = { servletContext ->
      bugsnagService.addMetadata = { metaData ->
        // do an inspection of the current application state
        def customfield1 = "" // assign meaningful value
        def customfield2 = "" // assign meaningful value
        
        // add the values to the metadata
        metaData.addToTab( "custom", "customfield1", customfield1 )
        metaData.addToTab( "custom", "customfield2", customfield2 )
      }
    }

## Implementation Notes
The plugin works by injecting a customized GrailsExceptionResolver into the application context (replacing the default object) which intercepts resolveException and reports using the bugsnagService before calling the superclass.

## Notes
future versions will include the ability to customize the messages sent to bugsnag to include user defined code.
