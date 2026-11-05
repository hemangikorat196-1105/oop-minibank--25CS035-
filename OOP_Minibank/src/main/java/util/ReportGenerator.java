package util;
import java.io.*; import java.nio.file.*; import java.nio.file.attribute.BasicFileAttributes;
public final class ReportGenerator {
    private ReportGenerator(){}
    public static String generate(Path folder,Path output)throws IOException{
        long deposits=0,withdrawals=0;StringBuilder report=new StringBuilder("MINIBANK END-OF-DAY REPORT\n");
        Files.createDirectories(folder);Files.createDirectories(output.toAbsolutePath().getParent());
        try(DirectoryStream<Path> stream=Files.newDirectoryStream(folder,"*.log")){
            for(Path file:stream){
                if(Files.size(file)==0)continue;
                try(BufferedReader reader=Files.newBufferedReader(file)){
                    String line;while((line=reader.readLine())!=null){String[] p=line.trim().split("\\s+");if(p.length<3)continue;
                        try{long amount=Long.parseLong(p[p.length-1]);if(p[0].equals("DEPOSIT"))deposits+=amount;else if(p[0].equals("WITHDRAW"))withdrawals+=amount;}catch(NumberFormatException ignored){}
                    }
                }
                BasicFileAttributes a=Files.readAttributes(file,BasicFileAttributes.class);
                report.append(file.getFileName()).append(" | size=").append(a.size()).append(" | modified=").append(a.lastModifiedTime()).append("\n");
            }
        }
        report.append("Total deposits: ").append(deposits).append("\nTotal withdrawals: ").append(withdrawals)
              .append("\nNet change: ").append(deposits-withdrawals).append("\n");
        Files.writeString(output,report.toString(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        return report.toString();
    }
}
