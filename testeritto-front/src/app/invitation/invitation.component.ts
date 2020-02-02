import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GroupService} from '../core/api/group.service';
import {InvitationService} from '../core/api/invitation.service';
import {UserService} from '../core/api/user.service';
import {Group} from '../core/models/group.model';
import {User} from '../core/models/user.model';

@Component({
    selector: 'app-invitation',
    templateUrl: './invitation.component.html',
    styleUrls: ['./invitation.component.css']
})
export class InvitationComponent implements OnInit {
    group: Group;
    user: User;
    link: string;
    errorMsg: string;
    isConsist: boolean;
    wrongLinkErr = false;

    constructor(private route: ActivatedRoute, private groupService: GroupService, private invitationService: InvitationService,
                private userService: UserService, private router: Router) {
        this.route.params.subscribe((params) => {
            this.link = params.link;
        });
    }

    ngOnInit() {
        this.userService.getUser().subscribe((user: User) => {
            this.user = user;
            this.invitationService.inviteUser(this.link).subscribe((group: Group) => {
                this.group = group;
                this.invitationService.isUserConsistInGroup(user.id, group.id).subscribe(value => {
                        this.isConsist = value;
                    },
                    error => {
                        this.errorMsg = error.error.message;
                    });

            }, () => this.wrongLinkErr = true);
        });
    }

    enterGroup(): void {
        this.userService.enterUserInGroup(this.user.id, this.group.id).subscribe(() => {
            this.router.navigate(['/group', this.group.id]);
        });
    }

    noEnterGroup(): void {
        this.router.navigate(['/group']);
    }
}


