package practice;

public class Main {
    public static void main(String[] args) {
        Runnable downloadTask = new DownloadTask();      // 다운로드 작업 정의
        Runnable logTask = new LogWriterTask();          // 로그 기록 작업 정의

        Thread downloadThread = new Thread(downloadTask);
        Thread logWriterThread = new Thread(logTask);

        downloadThread.start();
        logWriterThread.start();

        System.out.println("메인 스레드 종료");
    }
}