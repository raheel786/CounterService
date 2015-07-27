# CounterService
Source code for the code test requested.<BR>
A Jersey based REST webservice which has exposed following 3 methods <BR>
1- getCounter(counterName) Returns the value of the named counter<BR>
2- incrementCounter(counterName) increments the value of a named counter<BR>
3- getAll() Returns a list of all the counters and their values<BR>
<BR>
There are basically following 3 packages in the source code<BR>
<BR>
package com.innometrics.counter.service   -   Actual web service class is located in this package<BR>
package com.innometrics.counter.model     -   Java beans<BR>
package com.innometrics.counter.test;     -   Junit Test case, a very basic test case is also included in it which tests the functionality and also illustrates the usage of the web service. Please see the class description for details.


