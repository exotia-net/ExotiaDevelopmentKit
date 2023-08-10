package net.exotia.developer.kit.repositories;

import com.j256.ormlite.dao.Dao;
import net.exotia.developer.kit.DatabaseService;
import net.exotia.developer.kit.Scheduler;
import panda.std.function.ThrowingFunction;
import panda.std.reactive.Completable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AbstractRepository {
    protected final DatabaseService databaseService;
    protected final Scheduler scheduler;

    protected AbstractRepository(DatabaseService databaseService, Scheduler scheduler) {
        this.databaseService = databaseService;
        this.scheduler = scheduler;
    }

    <T> Completable<Dao.CreateOrUpdateStatus> save(Class<T> type, T warp) {
        return this.action(type, dao -> dao.createOrUpdate(warp));
    }

    <T> Completable<T> saveIfNotExist(Class<T> type, T warp) {
        return this.action(type, dao -> dao.createIfNotExists(warp));
    }

    <T, ID> Completable<T> select(Class<T> type, ID id) {
        return this.action(type, dao -> dao.queryForId(id));
    }

    <T, ID> Completable<Optional<T>> selectSafe(Class<T> type, ID id) {
        return this.action(type, dao -> Optional.ofNullable(dao.queryForId(id)));
    }

    <T> Completable<Integer> delete(Class<T> type, T warp) {
        return this.action(type, dao -> dao.delete(warp));
    }

    <T, ID> Completable<Integer> deleteById(Class<T> type, ID id) {
        return this.action(type, dao -> dao.deleteById(id));
    }

    <T> Completable<List<T>> selectAll(Class<T> type) {
        return this.action(type, Dao::queryForAll);
    }

    <T, ID, R> Completable<R> action(Class<T> type, ThrowingFunction<Dao<T, ID>, R, SQLException> action) {
        Completable<R> completable = new Completable<>();
        this.scheduler.runTaskAsynchronously(() -> {
            Dao<T, ID> dao = this.databaseService.getDao(type);
            try {
                completable.complete(action.apply(dao));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return completable;
    }
}
