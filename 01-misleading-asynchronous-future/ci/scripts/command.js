var Runtime = Java.type('java.lang.Runtime');
var File = Java.type("java.io.File");
var Scanner = Java.type("java.util.Scanner");
var Date =  Java.type("java.util.Date");
var InputStreamReader =  Java.type("java.io.InputStreamReader");
var BufferedReader =  Java.type("java.io.BufferedReader");
var Thread =  Java.type("java.lang.Thread");

function Command(workDir, cmd) {
    this.id = '';
    this.workDir = workDir;
    this.cmd = cmd;

    this.withId = function(id) {
        this.id = id;
        return this;
    }

    this.execute = function() {
        print(this.logPrefix() + 'Execute');
        var timeStarted = new Date().getTime();
        var result = -1;
        try {
            var process = this.executeSystemSpecific();
			this.consoleLog(process.getInputStream(), 'input');
            this.consoleLog(process.getErrorStream(), 'error');
			result = process.waitFor();
        } catch (exception) {
            exception.printStackTrace();
        }
        print(this.logPrefix() + 'Command finished with ' + (result === 0 ? 'success' : 'failure') + ' after ' + (new Date().getTime() - timeStarted) + ' millis');
        return result;
    }

    this.executeSystemSpecific = function() {
        return Runtime.getRuntime().exec(this.systemSpecificCommand(), null, new File(this.workDir))
    }

    this.systemSpecificCommand = function() {
        if (java.lang.System.getProperty("os.name").indexOf('Windows') >= 0) {
            if (typeof this.cmd === 'string') {
                return 'cmd.exe /C ' + this.cmd;
            } else {
                return ['cmd.exe', '/C'].concat(this.cmd);
            }
        }
        return this.cmd;
    }

    this.consoleLog = function(stream, type) {
        var thisObj = this;
		new Thread(function() {
            try {
                var inputStreamReader = new InputStreamReader(stream);
                var bufferedReader = new BufferedReader(inputStreamReader);
                var line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    print(thisObj.logPrefix() + line);
                }
            } catch (exception){
                exception.printStackTrace();
            }
        }).start();
    }

    this.logPrefix = function() {
        return this.id + ':' + this.workDir + ':' + this.cmd + ':';
    }
}