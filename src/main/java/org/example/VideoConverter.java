package org.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "VideoConverter", description = "Convert SBS 3D video to TAB format")
public class VideoConverter implements Callable<Integer> {

    @Option(names = "--input", required = true, description = "Input video file (SBS format expected)")
    private String inputFile;

    @Option(names = "--output", required = true, description = "Output video file (TAB format)")
    private String outputFile;

    @Option(names = "--format", required = true, description = "Output format (e.g., tab)")
    private String format;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new VideoConverter()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // 检查输入文件是否存在
        if (!Files.exists(Paths.get(inputFile))) {
            System.err.println("Input file does not exist: " + inputFile);
            return 1;
        }

        // 检查输入视频分辨率
        String resolution = getVideoResolution(inputFile);
        String actualInputFile = inputFile;

        // 如果分辨率不是 3840x1080（SBS 格式），则转换为 SBS
        if (!"3840x1080".equals(resolution)) {
            System.out.println("Input is not SBS format (3840x1080). Converting to SBS...");
            String tempSbsFile = "temp_sbs.m2ts";
            actualInputFile = convertToSBS(inputFile, tempSbsFile);
        }

        // 执行转换（SBS -> TAB）
        if ("tab".equalsIgnoreCase(format)) {
            String ffmpegCommand = String.format(
                    "ffmpeg -i %s -vf \"stereo3d=sbsl:abl\" -c:v libx264 -b:v 5M -c:a aac %s",
                    actualInputFile, outputFile
            );
            Process process = Runtime.getRuntime().exec(ffmpegCommand);
            int exitCode = process.waitFor();

            // 打印 FFmpeg 输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }

            if (exitCode != 0) {
                System.err.println("FFmpeg conversion failed with exit code: " + exitCode);
                return 1;
            }

            System.out.println("Video conversion successful: " + outputFile);
        } else {
            System.err.println("Unsupported format: " + format);
            return 1;
        }

        return 0;
    }

    // 检查输入视频分辨率
    private String getVideoResolution(String inputFile) throws Exception {
        Process process = new ProcessBuilder(
                "ffprobe", "-v", "error", "-select_streams", "v:0",
                "-show_entries", "stream=width,height", "-of", "csv=s=x:p=0", inputFile
        ).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String resolution = reader.readLine();
        process.waitFor();
        return resolution != null ? resolution.trim() : "";
    }

    // 转换为 SBS 格式
    private String convertToSBS(String inputFile, String tempSbsFile) throws Exception {
        String ffmpegCommand = String.format(
                "ffmpeg -i %s -vf \"hstack=inputs=2\" -c:v libx264 -c:a aac %s",
                inputFile, tempSbsFile
        );
        Process process = new ProcessBuilder(ffmpegCommand.split(" ")).start();
        int exitCode = process.waitFor();

        // 打印 FFmpeg 输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line);
        }

        if (exitCode != 0) {
            throw new RuntimeException("Failed to convert to SBS format with exit code: " + exitCode);
        }

        return tempSbsFile;
    }
}