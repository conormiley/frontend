// @flow
import { isExpired } from 'common/modules/experiments/test-can-run-checks';
import { removeParticipation } from 'common/modules/experiments/utils';
import { getTest as getAcquisitionTest } from 'common/modules/experiments/acquisition-test-selector';
import { commercialPrebidAdYouLike } from 'common/modules/experiments/tests/commercial-prebid-adyoulike.js';
import { commercialPrebidSafeframe } from 'common/modules/experiments/tests/commercial-prebid-safeframe.js';
import { commercialAdVerification } from 'common/modules/experiments/tests/commercial-ad-verification.js';
import { FootballWeeklyTreatVsContainer } from 'common/modules/experiments/tests/football-weekly-treat-vs-container';

export const TESTS: $ReadOnlyArray<ABTest> = [
    getAcquisitionTest(),
    commercialPrebidSafeframe,
    commercialPrebidAdYouLike,
    commercialAdVerification,
    FootballWeeklyTreatVsContainer,
].filter(Boolean);

export const getActiveTests = (): $ReadOnlyArray<ABTest> =>
    TESTS.filter(test => {
        if (isExpired(test.expiry)) {
            removeParticipation(test);
            return false;
        }
        return true;
    });

export const getExpiredTests = (): $ReadOnlyArray<ABTest> =>
    TESTS.filter(test => isExpired(test.expiry));

export const getTest = (id: string): ?ABTest => {
    const testIds = TESTS.map(test => test.id);
    const index = testIds.indexOf(id);
    return index > -1 ? TESTS[index] : null;
};
