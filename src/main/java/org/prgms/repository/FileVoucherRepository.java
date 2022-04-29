package org.prgms.repository;

import org.prgms.domain.Voucher;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
//@Profile("dev")
public class FileVoucherRepository implements VoucherRepository {
    private static final File objectFolder = new File("./objects");
    private static final String objPattern = "*.obj";

    private static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + objPattern);

    public FileVoucherRepository() {
        if (!objectFolder.exists())
            objectFolder.mkdir();
    }

    @Override
    public void save(Voucher voucher) {
        String filename = String.format("./objects/%s.obj", voucher.getVoucherId().toString());

        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(voucher);

        } catch (IOException e) {
            throw new RuntimeException(MessageFormat.format("해당하는 파일이 존재하지 않습니다. msg : {0}", e.getMessage()));
        }
    }

    @Override
    public List<Voucher> findAll() {
        try (Stream<Path> fileStream = Files.list(Paths.get(objectFolder.getPath()))) {

            return fileStream
                    .filter(path -> matcher.matches(path.getFileName()))
                    .map(this::deserializeVoucher).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(MessageFormat.format("해당하는 폴더가 존재하지 않습니다. msg : {0}", e.getMessage()));
        }
    }

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        try (Stream<Path> fileStream = Files.list(Paths.get(objectFolder.getPath()))) {

            Optional<Path> targetPath = fileStream
                    .filter(path -> matcher.matches(path.getFileName()))
                    .filter(path -> path.endsWith(voucherId.toString() + ".obj")).findFirst();

            if (targetPath.isEmpty())
                return Optional.empty();

            return Optional.of(this.deserializeVoucher(targetPath.get()));

        } catch (IOException e) {
            throw new RuntimeException(MessageFormat.format("해당하는 폴더가 존재하지 않습니다. msg : {0}", e.getMessage()));
        }
    }

    @Override
    public void deleteAll() {
        try (Stream<Path> files = Files.list(Paths.get(objectFolder.getPath()))) {

            files.filter(path -> matcher.matches(path.getFileName()))
                    .forEach(path -> path.toFile().delete());

        } catch (IOException e) {
            throw new RuntimeException(MessageFormat.format("해당하는 폴더가 존재하지 않습니다. msg : {0}", e.getMessage()));
        }
    }

    private Voucher deserializeVoucher(Path path) {
        try {
            Object obj = new ObjectInputStream(new FileInputStream(path.toString())).readObject();

            return (Voucher) obj;
        } catch (IOException e) {

            throw new RuntimeException(
                    MessageFormat.format("해당하는 파일이 존재하지 않습니다. msg : {0}", e.getMessage()));

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(MessageFormat.format("deserialization에서 문제가 발생했습니다.. msg : {0}", e.getMessage()));
        }
    }
}
