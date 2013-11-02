# Bugsnag Grails Plugin

Current Version: 0.1

The bugsnag grails plugin integrates the bugsnag reporting client into your grails applications and automatically reports errors to the bugsnag service (www.bugsnag.com). Information reported by the client includes a dump of the original request (headers, cookies, query, original URL, grails environment, etc), information about the currently logged in user, and a stacktrace of the error that occurred.

# Installation

compile ":cookie-session:0.1"

# Usage

Simply enabling bugsnag enables error reporting capabilities. All uncaught exceptions will be reported to bugsnag. Additionally, you can use the bugsnagService directly to send notifications to bugsnag.

To use the bugsnagService, add it to your services or controllers as follows:

  def bugsnagService

Bugsnag service has two methods:

    def getConfiguredClient(def context) : return a configured bugsnag client (https://bugsnag.com/docs/notifiers/java)
    def notify(def request, def exception) : send a notification to bugsnag, passing in the current request 

# Configuration

## Parameters
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


# Implementation Notes
The plugin works by injecting a customized GrailsExceptionResolver which intercepts resolveException and reports them bugsnag before calling the superclass.

# Notes
future versions will include the ability to customize the messages sent to bugsnag to include user defined code.
