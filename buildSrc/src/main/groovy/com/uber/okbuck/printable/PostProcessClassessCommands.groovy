package com.uber.okbuck.printable

public class PostProcessClassessCommands implements Printable {

    private static final DEPENDENCIES_CLASSPATH = "DEPENDENCIES_CLASSPATH"
    private static final POSTPROCESS_CLASSPATH = "POSTPROCESS_CLASSPATH"

    private final List<String> mPostprocessClassesCommands;
    private final Set<String> mPostProcessDeps
    private final String mBootClasspath;
    private final String mGenDir;

    public PostProcessClassessCommands(String bootClasspath, String genDir, Set<String> postProcessDeps,
                                       List<String> postprocessClassesCommands = []) {
        mPostprocessClassesCommands = postprocessClassesCommands
        mBootClasspath = bootClasspath
        mGenDir = genDir
        mPostProcessDeps = postProcessDeps
    }

    @Override
    public void print(PrintStream printer) {
        if (!mPostprocessClassesCommands.isEmpty()) {
            String deps = "\$(JARS=(`find ${mGenDir} ! -name \"*-abi.jar\" ! -name \"*dex.dex.jar\" -name \"*.jar\"`); " +
                    "CLASSPATH=${mBootClasspath}; IFS=:; MERGED=( \"\${JARS[@]}\" \"\${CLASSPATH[@]}\" ); " +
                    "echo \"\${MERGED[*]}\")"
            String commandClassPath = mPostProcessDeps.join(":")
            printer.println("\tpostprocess_classes_commands = [")
            mPostprocessClassesCommands.each {
                String command -> printer.println("\t\t'${DEPENDENCIES_CLASSPATH}=${deps} ${POSTPROCESS_CLASSPATH}=${commandClassPath} ${command}',")
            }
            printer.println("\t],")
        }
    }
}
