<<<<<<< HEAD
# VideoConverter
=======
# VideoConverter 

一个将 Side-by-Side (SBS) 格式的 3D 视频转换为 Top-and-Bottom (TAB) 格式的 CLI 程序。 

## 概述 

本程序接受 SBS 格式的 3D 视频作为输入，并使用 FFmpeg 将其转换为 TAB 格式。如果输入视频不是 SBS 格式（分辨率不是 3840x1080），程序会自动将其转换为 SBS 格式后再进行 TAB 转换。 

## 运行说明 

### 运行依赖 

**Java 17**：用于运行 JAR 文件。  

### 安装方法（以 macOS 为例）

bash    brew install openjdk@17

- 验证安装：

  bash

  收起自动换行复制

  `java -version`

  确保输出显示 Java 17，例如：

  text

  收起自动换行复制

  `openjdk version "17.0.14" 2025-01-14`

- FFmpeg

  ：用于视频转换。

  - 安装方法（以 macOS 为例）：

    bash

    收起自动换行复制

    `brew install ffmpeg`

  - 安装方法（以 Ubuntu 为例）：

    bash

    收起自动换行复制

    `sudo apt-get install ffmpeg`

  - 验证安装：

    bash

    收起自动换行复制

    `ffmpeg -version`

    确保 FFmpeg 已正确安装并在 PATH 中。

### 运行环境

- 本程序已在 macOS 系统中测试通过，理论上也支持 Linux 和 Windows 系统（需要安装 Java 17 和 FFmpeg）。
- 确保输入视频文件存在且可访问。

## 构建

1. 克隆仓库：

   bash

   收起自动换行复制

   `git clone <你的 Git 仓库链接> cd VideoConverter`

2. 使用 Maven 构建项目：

   bash

   收起自动换行复制

   `mvn clean package`

   构建完成后，会在 

   target/

    目录下生成 

   VideoConverter-1.0-SNAPSHOT-jar-with-dependencies.jar

   。

## 使用方法

使用以下命令运行程序：

bash

收起自动换行复制

```
java -jar target/VideoConverter-1.0-SNAPSHOT-jar-with-dependencies.jar --input <输入视频> --output <输出视频> --format tab
```

### 示例

将 input.m2ts 转换为 output_tab.mp4（TAB 格式）：

bash

收起自动换行复制

```
java -jar target/VideoConverter-1.0-SNAPSHOT-jar-with-dependencies.jar --input input.m2ts --output output_tab.mp4 --format tab
```

- 如果 input.m2ts 不是 SBS 格式（分辨率不是 3840x1080），程序会自动将其转换为 SBS 格式（生成临时文件 temp_sbs.m2ts），然后再转换为 TAB 格式。
- 输出视频 output_tab.mp4 将是 TAB 格式（分辨率 1920x2160）。

## 注意事项

- 程序使用 FFmpeg 的 stereo3d 滤镜（stereo3d=sbsl:abl）将 SBS 格式转换为 TAB 格式。
- 请确保 FFmpeg 已安装并在 PATH 环境变量中。
- 程序经过测试支持 H.264 格式的视频。其他格式可能需要额外的 FFmpeg 配置。
- 转换过程中会生成临时文件 temp_sbs.m2ts，转换完成后可手动删除。

## 依赖

所有 Java 依赖已通过 maven-assembly-plugin 打包到 JAR 文件中，无需额外提供。

- picocli:4.7.5：用于 CLI 参数解析。
- slf4j-api:2.0.9 和 logback-classic:1.4.14：用于日志记录。

## 提交信息

- 作者：郝广嘉
- Git 仓库：[你的 Git 仓库链接]
- 语言：Java

>>>>>>> 881c524 (Submit VideoConverter CLI program for SBS to TAB conversion)
